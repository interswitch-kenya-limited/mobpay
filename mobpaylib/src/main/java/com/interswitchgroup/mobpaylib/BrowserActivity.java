package com.interswitchgroup.mobpaylib;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class BrowserActivity extends AppCompatActivity {

    private String topic;
    private MqttClient sampleClient;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        topic = getIntent().getStringExtra("topic");
        try {
            String mqttServer = getIntent().getStringExtra("mqttServer");
            sampleClient = new MqttClient(mqttServer, UUID.randomUUID().toString(), new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + mqttServer);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    Log.i(this.getClass().getSimpleName(), "Message: " + topic + " : " + new String(message.getPayload()));
                    finish();
                }

            });
        } catch (MqttException me) {
            me.printStackTrace();
        }
        WebView webview = findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setLoadWithOverviewMode(false);
//        settings.setDisplayZoomControls(false);
//        settings.setBuiltInZoomControls(false);
//        settings.setUseWideViewPort(false);
//        settings.setLoadWithOverviewMode(false);
//        webview.setInitialScale(50);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log the URL here
                Log.i(this.getClass().getSimpleName(), "Loading URL: " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Log the URL when the page starts loading
                Log.i(this.getClass().getSimpleName(), "Page started: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Log the URL when the page finishes loading
                Log.i(this.getClass().getSimpleName(), "Page finished: " + url);
            }
        });
        String url = getIntent().getStringExtra("url");
        Log.i(this.getClass().getSimpleName(), url);
        webview.loadUrl(url);
    }
}
