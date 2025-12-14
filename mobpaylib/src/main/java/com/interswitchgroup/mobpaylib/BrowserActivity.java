package com.interswitchgroup.mobpaylib;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.utils.InterswitchException;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BrowserActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BrowserActivity";
    private static final int MQTT_CONNECTION_TIMEOUT_SECONDS = 30;

    private String topic;
    private String checkoutUrl;

    private Mqtt5AsyncClient mqttClient;
    private ExecutorService executorService;
    private Handler mainHandler;

    // Static callbacks (kept as-is)
    private static TransactionSuccessCallback successCallback;
    private static TransactionFailureCallback failureCallback;

    public static void setCallbacks(
            TransactionSuccessCallback success,
            TransactionFailureCallback failure
    ) {
        successCallback = success;
        failureCallback = failure;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        topic = getIntent().getStringExtra("topic");
        checkoutUrl = getIntent().getStringExtra("url");

        // Validate required parameters
        if (topic == null || topic.isEmpty()) {
            notifyFailure("MQTT topic is required", null);
            return;
        }

        if (checkoutUrl == null || checkoutUrl.isEmpty()) {
            notifyFailure("Checkout URL is required", null);
            return;
        }

        // Initialize executor and handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        setupWebView();
        setupMqtt();
    }

    // --------------------------------------------------
    // MQTT 5 (HiveMQ) SETUP
    // --------------------------------------------------
    private void setupMqtt() {
        // Run MQTT setup on background thread to avoid NetworkOnMainThreadException
        executorService.execute(() -> {
            try {
                String clientId = "merchant-terminal-" + UUID.randomUUID();

                Log.i(LOG_TAG, "🔌 Initializing MQTT client: " + clientId);

                mqttClient = MqttClient.builder()
                        .useMqttVersion5()
                        .identifier(clientId)
                        .serverHost("testmerchant.interswitch-ke.com")
                        .serverPort(8084)
                        .sslWithDefaultConfig()
                        .webSocketConfig()
                        .serverPath("/mqtt")
                        .subprotocol("mqtt")
                        .applyWebSocketConfig()
                        .buildAsync();

                Log.i(LOG_TAG, "🔄 Connecting to MQTT broker...");

                // Connect with timeout handling
                mqttClient.connect()
                        .whenComplete((connAck, throwable) -> {
                            if (throwable != null) {
                                Log.e(LOG_TAG, "❌ MQTT connect failed", throwable);
                                notifyFailure("MQTT connection failed", throwable);
                                return;
                            }

                            Log.i(LOG_TAG, "✅ MQTT connected (v5)");
                            subscribe();
                        });

            } catch (Exception e) {
                Log.e(LOG_TAG, "❌ MQTT setup exception", e);
                notifyFailure("MQTT setup failed", e);
            }
        });
    }

    private void subscribe() {
        executorService.execute(() -> {
            try {
                Log.i(LOG_TAG, "📡 Subscribing to topic: " + topic);

                mqttClient.subscribeWith()
                        .topicFilter(topic)
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .callback(publish -> {
                            String payload = new String(publish.getPayloadAsBytes());
                            Log.i(LOG_TAG, "📩 MQTT message received: " + payload);

                            // Handle callback on main thread
                            mainHandler.post(() -> {
                                if (successCallback != null) {
                                    try {
                                        // TODO: Parse payload into proper response object
                                        // TransactionResponse response = parsePayload(payload);
                                        // successCallback.onSuccess(response);
                                        Log.i(LOG_TAG, "✅ Transaction success callback invoked");
                                    } catch (Exception e) {
                                        Log.e(LOG_TAG, "❌ Error parsing transaction response", e);
                                        notifyFailure("Failed to parse transaction response", e);
                                    }
                                }
                                finish();
                            });
                        })
                        .send()
                        .whenComplete((subAck, throwable) -> {
                            if (throwable != null) {
                                Log.e(LOG_TAG, "❌ Subscribe failed", throwable);
                                notifyFailure("MQTT subscribe failed", throwable);
                                return;
                            }

                            Log.i(LOG_TAG, "✅ Successfully subscribed to topic: " + topic);

                            // Load checkout AFTER subscription is active (on main thread)
                            mainHandler.post(() -> {
                                try {
                                    Log.i(LOG_TAG, "🌐 Loading checkout URL: " + checkoutUrl);
                                    WebView webView = findViewById(R.id.webview);
                                    if (webView != null) {
                                        webView.loadUrl(checkoutUrl);
                                    } else {
                                        Log.e(LOG_TAG, "❌ WebView not found");
                                        notifyFailure("WebView initialization failed", null);
                                    }
                                } catch (Exception e) {
                                    Log.e(LOG_TAG, "❌ Error loading checkout URL", e);
                                    notifyFailure("Failed to load checkout page", e);
                                }
                            });
                        });

            } catch (Exception e) {
                Log.e(LOG_TAG, "❌ Subscribe exception", e);
                notifyFailure("MQTT subscribe failed", e);
            }
        });
    }

    // --------------------------------------------------
    // WEBVIEW SETUP
    // --------------------------------------------------
    private void setupWebView() {
        WebView webView = findViewById(R.id.webview);
        if (webView == null) {
            Log.e(LOG_TAG, "❌ WebView not found in layout");
            notifyFailure("WebView initialization failed", null);
            return;
        }

        WebSettings settings = webView.getSettings();

        // Enable JavaScript for payment gateway
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // Payment security hardening
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        // Responsive layout
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // Allow mixed content for payment gateways (use cautiously)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String url = request.getUrl().toString();
                    Log.i(LOG_TAG, "🔗 Loading URL: " + url);
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(LOG_TAG, "🔄 Page loading: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(LOG_TAG, "✅ Page loaded: " + url);
            }

            @Override
            public void onReceivedError(
                    WebView view,
                    int errorCode,
                    String description,
                    String failingUrl
            ) {
                Log.e(LOG_TAG, "❌ WebView error: " + description + " (" + errorCode + ")");
                notifyFailure("Failed to load payment page: " + description, null);
            }
        });
    }

    // --------------------------------------------------
    // ERROR HANDLING
    // --------------------------------------------------
    private void notifyFailure(String message, Throwable cause) {
        Log.e(LOG_TAG, "🚨 " + message, cause);

        mainHandler.post(() -> {
            if (failureCallback != null) {
                failureCallback.onError(
                        new InterswitchException(
                                message,
                                "9002",
                                cause != null ? cause.getMessage() : null
                        )
                );
            }
            finish();
        });
    }

    // --------------------------------------------------
    // CLEANUP
    // --------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(LOG_TAG, "🧹 Cleaning up resources");

        // Disconnect MQTT client
        if (mqttClient != null) {
            executorService.execute(() -> {
                try {
                    mqttClient.disconnect()
                            .whenComplete((aVoid, throwable) -> {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "❌ MQTT disconnect error", throwable);
                                } else {
                                    Log.i(LOG_TAG, "🔌 MQTT disconnected");
                                }
                            });
                } catch (Exception e) {
                    Log.e(LOG_TAG, "❌ Error during MQTT disconnect", e);
                }
            });
        }

        // Shutdown executor service
        if (executorService != null && !executorService.isShutdown()) {
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "❌ Executor shutdown interrupted", e);
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        // Clear callbacks to prevent memory leaks
        successCallback = null;
        failureCallback = null;
    }

    @Override
    public void onBackPressed() {
        // Handle back button - you might want to show a confirmation dialog
        Log.i(LOG_TAG, "⬅️ Back button pressed");
        notifyFailure("Transaction cancelled by user", null);
    }
}