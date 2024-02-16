package com.interswitchgroup.mobpaylib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.CheckoutTransactionPayload;
import com.interswitchgroup.mobpaylib.api.model.MerchantConfigResponse;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.PaybillQueryResponse;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentResponse;
import com.interswitchgroup.mobpaylib.api.service.Checkout;
import com.interswitchgroup.mobpaylib.api.service.MerchantConfig;
import com.interswitchgroup.mobpaylib.api.service.MobilePayment;
import com.interswitchgroup.mobpaylib.api.service.PesalinkPayment;
import com.interswitchgroup.mobpaylib.api.service.TranscationConfirmation;
import com.interswitchgroup.mobpaylib.di.DaggerWrapper;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkSuccessCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.utils.AESEncryptor;
import com.interswitchgroup.mobpaylib.utils.NullChecker;
import com.interswitchgroup.mobpaylib.utils.RSAUtil;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MobPay implements Serializable {
    private String mqttServer;
    private String checkoutUrl;
    private static MobPay singletonMobPayInstance;
    private static final String LOG_TAG = MobPay.class.getSimpleName();
    private String clientId;
    private String clientSecret;
    private Retrofit retrofit;
    private TransactionFailureCallback transactionFailureCallback;
    private TransactionSuccessCallback transactionSuccessCallback;
    private MerchantConfigResponse.Config merchantConfig;
    private static Config config = new Config();
    private Activity activity;
    private ApplicationInfo ai;
    boolean receivedMessage = false;

    private MobPay() {
    }

    public static MobPay getInstance(Activity activity, String clientId, String clientSecret, Config config) throws Exception {
        if (singletonMobPayInstance == null) {
            singletonMobPayInstance = new MobPay();
            DaggerWrapper.getComponent(activity, clientId, clientSecret).inject(singletonMobPayInstance);
            singletonMobPayInstance.clientId = clientId;
            singletonMobPayInstance.clientSecret = clientSecret;
            singletonMobPayInstance.activity = activity;
            singletonMobPayInstance.ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            singletonMobPayInstance.mqttServer = String.valueOf(singletonMobPayInstance.ai.metaData.get("interswitch-kenya-limited.mobpay.mqtt_url"));
            singletonMobPayInstance.checkoutUrl = String.valueOf(singletonMobPayInstance.ai.metaData.get("interswitch-kenya-limited.mobpay.checkout_url"));
        }

        // If enabled channels was explicitly passed, override default enabled channels
        return singletonMobPayInstance;
    }

    @Inject
    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public TransactionFailureCallback getTransactionFailureCallback() {
        return transactionFailureCallback;
    }

    public TransactionSuccessCallback getTransactionSuccessCallback() {
        return transactionSuccessCallback;
    }

    public static Config getConfig() {
        return config;
    }

    private void initializeMerchantConfig() throws Exception {
        MerchantConfigResponse merchantConfigResponse = new MerchantConfigInitializationTask().execute().get();
        if (merchantConfigResponse != null) {
            merchantConfig = merchantConfigResponse.getConfig();
        } else {
            throw new Exception("Failed to fetch merchant config, check your internet and configuration");
        }
    }

    public MerchantConfigResponse.Config getMerchantConfig() {
        return merchantConfig;
    }

    private static class MerchantConfigInitializationTask extends AsyncTask<String, Void, MerchantConfigResponse> {

        @Override
        protected MerchantConfigResponse doInBackground(String... params) {
            try {
                return singletonMobPayInstance.retrofit.create(MerchantConfig.class)
                        .fetchMerchantConfig()
                        .execute()
                        .body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MerchantConfigResponse result) {
            if (result != null) {
                singletonMobPayInstance.merchantConfig = result.getConfig();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /**
     * This method should take an invoice and launch a ui to take the user's preferred method of payment
     * and related details e.g. payment mode card and required card details.
     * Then once collected it will call the interswitch backend to make the payment and
     * call onSuccess or onFailure of the passed transaction callback
     *
     * @param merchant
     * @param payment
     * @param customer
     * @param transactionSuccessCallback
     * @param transactionFailureCallback
     */

    public void pay(Activity activity, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        try {
            NullChecker.checkNull(merchant, "merchant must not be null");
            NullChecker.checkNull(payment, "payment must not be null");
            NullChecker.checkNull(customer, "customer must not be null");
            NullChecker.checkNull(transactionSuccessCallback, "transactionSuccessCallback must not be null");
            NullChecker.checkNull(transactionFailureCallback, "transactionFailureCallback must not be null");
            customer.validate();
            setReceivedMessage(false);
            //TODO: create validation for all classes
            checkout(activity, merchant, payment, customer, transactionFailureCallback);
            final String topic = "merchant_portal/" + merchant.getMerchantId() + "/" + payment.getTransactionRef();

            final MqttClient sampleClient = new MqttClient(mqttServer, UUID.randomUUID().toString(), new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            Log.i(LOG_TAG, "Connecting to broker: " + mqttServer);
            sampleClient.connect(connOpts);
            Log.i(LOG_TAG,"Connected");
            sampleClient.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
                    // message Arrived!
                    Log.i(LOG_TAG,"Message: " + topic + " : " + new String(message.getPayload()));
                    /**
                     * Run on ui thread otherwise utakua mwingi wa machozi
                     */
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CardPaymentResponse cardPaymentResponse = new ObjectMapper()
                                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                        .readValue(new String(message.getPayload()), CardPaymentResponse.class);
                                if (cardPaymentResponse.getTransactionRef() == null || cardPaymentResponse.getTransactionRef().isEmpty()) {
                                    throw new Exception("Invalid response");
                                }
                                if (!isReceivedMessage()) {
                                    setReceivedMessage(true);
                                    if (cardPaymentResponse.getResponseCode().equals("00")) {
                                        transactionSuccessCallback.onSuccess(cardPaymentResponse);
                                    } else {
                                        transactionFailureCallback.onError(new Exception(cardPaymentResponse.toString()));
                                    }
                                }
                            } catch (Exception e) {
                                if (!isReceivedMessage()) {
                                    transactionFailureCallback.onError(new Exception(new String(message.getPayload())));
                                }
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }

    }

    /*
    needs to return the url and topic
    pass checkout dto
     */

    private void checkout(Activity activity, Merchant merchant, Payment payment, Customer customer, TransactionFailureCallback transactionFailureCallback) {
        Retrofit ipgBackendRetrofit = new Retrofit.Builder()
                .baseUrl(checkoutUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CheckoutTransactionPayload checkoutTransactionPayload = new CheckoutTransactionPayload(merchant, payment, customer, config);
        Checkout checkoutService = ipgBackendRetrofit.create(Checkout.class);
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(checkoutTransactionPayload, Map.class);
        map.values().removeAll(Collections.singleton(null));
        Call checkoutServiceCall = checkoutService.transactionCheckout(map);
        final String topic = "merchant_portal/" + merchant.getMerchantId() + "/" + payment.getTransactionRef();

        checkoutServiceCall.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Log.i(LOG_TAG, "Checkout URL: " + response.raw().request().url().toString());
                Intent intent = new Intent(activity, BrowserActivity.class);
                intent.putExtra("url", response.raw().request().url().toString());
                intent.putExtra("mqttServer", mqttServer);
                intent.putExtra("topic", topic);
                if (response.isSuccessful()){
                    activity.startActivity(intent);
                }
                transactionFailureCallback.onError(new Error(response.message()));

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage());
                transactionFailureCallback.onError(t);
            }
        });
    }

    @Deprecated
    public void makeCardPayment(Card card, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {

    }

    @Deprecated
    public void makeCardTokenPayment(CardToken cardToken, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {

    }

    @Deprecated
    public void makeMobileMoneyPayment(Mobile mobile, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {

    }

    @Deprecated
    public void confirmMobilePayment(String orderId, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {

    }

    public enum PaymentChannel {
        CARD("Card"), MOBILE("Mobile"), BANK("Bank"), PAYCODE("Verve Paycode"), PAYFROMPESALINK("Pesalink");
        public String value;

        PaymentChannel(String value) {
            this.value = value;
        }
    }

    public void makePesalinkPayment(Merchant merchant, Payment payment, Customer customer, final PesalinkSuccessCallback pesalinkSuccessCallback, final PesalinkFailureCallback pesalinkFailureCallback) {
    }

    public void confirmTransactionPayment(String transactionRef, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        try {
            Disposable subscribe = retrofit.create(TranscationConfirmation.class)
                    .confirmTransanction(transactionRef)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PaybillQueryResponse>() {
                        @Override
                        public void accept(PaybillQueryResponse transactionPaymentResponse) {
                            Log.i(LOG_TAG, "Transaction payment succeeded, ref:\t" + transactionPaymentResponse.getTransactionRef());
                            transactionSuccessCallback.onSuccess(transactionPaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(LOG_TAG, "Transaction payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public boolean isReceivedMessage() {
        return receivedMessage;
    }

    public void setReceivedMessage(boolean receivedMessage) {
        this.receivedMessage = receivedMessage;
    }
}
