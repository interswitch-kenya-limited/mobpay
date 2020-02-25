package com.interswitchgroup.mobpaylib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.MerchantConfigResponse;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.PaybillQueryResponse;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MobPay implements Serializable {
    private String mqttServer;
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
        }

        if (singletonMobPayInstance.getMerchantConfig() == null) {
            singletonMobPayInstance.initializeMerchantConfig();
        }

        // If enabled channels was explicitly passed, override default enabled channels
        if (config != null) {
            //TODO add pesalink here
            if (!"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getBnkStatus())) {
                config.getChannels().remove(PaymentChannel.BANK);
            }
            if (!"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getMmoStatus())) {
                if (!"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getAirtelStatus())
                        && !"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getMpesaStatus())
                        && !"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getEquitelStatus())
                        && !"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getTkashStatus())) {
                    //Mobile payment option is only removed if main mmo config is not true and all provider channels are missing or disabled too
                    config.getChannels().remove(PaymentChannel.MOBILE);
                }
            }
            if (!"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getCardStatus())) {
                config.getChannels().remove(PaymentChannel.CARD);
            }
            if (!"1".equalsIgnoreCase(singletonMobPayInstance.merchantConfig.getPaycodeStatus())) {
                config.getChannels().remove(PaymentChannel.PAYCODE);
            }
            MobPay.config = config;
        }
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
        NullChecker.checkNull(activity, "Activity context must not be null");
        NullChecker.checkNull(merchant, "merchant must not be null");
        NullChecker.checkNull(customer, "customer must not be null");
        NullChecker.checkNull(payment, "payment must not be null");
        NullChecker.checkNull(transactionSuccessCallback, "transactionSuccessCallback must not be null");
        NullChecker.checkNull(transactionFailureCallback, "transactionFailureCallback must not be null");

        this.transactionSuccessCallback = transactionSuccessCallback;
        this.transactionFailureCallback = transactionFailureCallback;

        Intent intent = new Intent(activity, MobPayActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("merchant", merchant);
        intent.putExtra("customer", customer);
        intent.putExtra("payment", payment);
        intent.putExtra("clientId", clientId);
        intent.putExtra("clientSecret", clientSecret);
        activity.startActivity(intent);
        /*
        Launch ui
        collect card data
        build card payment payload
        send card payment request
        on response call appropriate transaction callback method
         */
    }

    public void makeCardPayment(Card card, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(card, "card must not be null");
        payment.setPreauth(String.valueOf(merchantConfig.getCardPreauth() != null ? merchantConfig.getCardPreauth() : 0));
        try {
            String modulus = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.modulus"));
            String exponent = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.exponent"));
            PublicKey publicKey = RSAUtil.getPublicKey(modulus, exponent);
            String authData = RSAUtil.getAuthDataMerchant(publicKey, card.getPan(), card.getCvv(), card.getExpiryYear() + card.getExpiryMonth(), card.isTokenize() ? 1 : 0, "D");
            CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(merchant, payment, customer, authData);
            String payloadString = cardPaymentPayload.toString();
            String sdkcardinalurl = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.cardinal_url"));
            Uri url = Uri.parse(sdkcardinalurl);
            String public3dsPayloadRSAKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjJ84cM/HJEOvuxxWwbOTsF+GeFD7qQCMaSSbfWo7x0oiNEMxRGZOCPpQI+SNt8D4n+U4YroRmo4W4wgNkkJWQJkx5EyDJePGv5NSGXW+27uQpOin7G2h7JAHq+mF3hcR4uR7GlMw4MpTdNyYfb2L/8RvCdIXzANQOpdNFsbNm62qJSOO/gq1jCTl/+8HudIQHR7Vyw1QrL+3Sp0ZlkzlUr2SouPVyEVodcea2z4gkH1AQMwXGXUzALMqtYo3uUaOZb5E3vKDzTeTkVzujefloPUxVBJXfW0ypkH452ccOywH6Fv/aJaVUvQCe5arEO4IPg9HjsWrxsqkvZ2xnPrkfQIDAQAB";
            String randomUUID = UUID.randomUUID().toString();
            int keyLength = 16;
            String aesKey = randomUUID.substring(randomUUID.length() - keyLength);
            String encryptedKey = Base64.encodeToString(RSAUtil.encrypt(aesKey.getBytes(), public3dsPayloadRSAKey), Base64.NO_WRAP);
            randomUUID = UUID.randomUUID().toString();//Regenerate the uuid to use as an iv
            String iv = randomUUID.substring(randomUUID.length() - keyLength);
            String encryptedIv = Base64.encodeToString(RSAUtil.encrypt(iv.getBytes(), public3dsPayloadRSAKey), Base64.NO_WRAP);
            url = url.buildUpon()
                    .appendQueryParameter("transactionType", "CARD")
                    .appendQueryParameter("key", encryptedKey)
                    .appendQueryParameter("iv", encryptedIv)
                    .appendQueryParameter("payload", AESEncryptor.encrypt(aesKey, iv, payloadString))
                    .build();
            Intent intent = new Intent(activity, BrowserActivity.class);
            intent.putExtra("url", url.toString());
            intent.putExtra("mqttServer", mqttServer);
            final String topic = "merchant_portal/" + merchant.getMerchantId() + "/" + payment.getTransactionRef();
            intent.putExtra("topic", topic);
            activity.startActivity(intent);
            try {
                final MqttClient sampleClient = new MqttClient(mqttServer, UUID.randomUUID().toString(), new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                connOpts.setAutomaticReconnect(true);
                System.out.println("Connecting to broker: " + mqttServer);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                sampleClient.subscribe(topic, new IMqttMessageListener() {
                    @Override
                    public void messageArrived(String topic, final MqttMessage message) throws Exception {
                        // message Arrived!
                        System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
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
                                    transactionSuccessCallback.onSuccess(cardPaymentResponse);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    transactionFailureCallback.onError(new Exception(new String(message.getPayload())));
                                }
                            }
                        });
                    }
                });
            } catch (MqttException me) {
                me.printStackTrace();
            }
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public void makeCardTokenPayment(CardToken cardToken, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(cardToken, "cardToken must not be null");
        payment.setPreauth(String.valueOf(merchantConfig.getCardPreauth() != null ? merchantConfig.getCardPreauth() : 0));
        try {
            String modulus = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.modulus"));
            String exponent = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.exponent"));
            PublicKey publicKey = RSAUtil.getPublicKey(modulus, exponent);
            String authData = RSAUtil.getAuthDataMerchant(publicKey, cardToken.getToken(), cardToken.getCvv(), cardToken.getExpiry().replaceAll("[^\\d]", ""), 0, ",");
            CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(merchant, payment, customer, authData);
            String payloadString = cardPaymentPayload.toString();
            String sdkcardinalurl = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.cardinal_url"));
            Uri url = Uri.parse(sdkcardinalurl);
            String public3dsPayloadRSAKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjJ84cM/HJEOvuxxWwbOTsF+GeFD7qQCMaSSbfWo7x0oiNEMxRGZOCPpQI+SNt8D4n+U4YroRmo4W4wgNkkJWQJkx5EyDJePGv5NSGXW+27uQpOin7G2h7JAHq+mF3hcR4uR7GlMw4MpTdNyYfb2L/8RvCdIXzANQOpdNFsbNm62qJSOO/gq1jCTl/+8HudIQHR7Vyw1QrL+3Sp0ZlkzlUr2SouPVyEVodcea2z4gkH1AQMwXGXUzALMqtYo3uUaOZb5E3vKDzTeTkVzujefloPUxVBJXfW0ypkH452ccOywH6Fv/aJaVUvQCe5arEO4IPg9HjsWrxsqkvZ2xnPrkfQIDAQAB";
            String randomUUID = UUID.randomUUID().toString();
            int keyLength = 16;
            String aesKey = randomUUID.substring(randomUUID.length() - keyLength);
            String encryptedKey = Base64.encodeToString(RSAUtil.encrypt(aesKey.getBytes(), public3dsPayloadRSAKey), Base64.NO_WRAP);
            randomUUID = UUID.randomUUID().toString();//Regenerate the uuid to use as an iv
            String iv = randomUUID.substring(randomUUID.length() - keyLength);
            String encryptedIv = Base64.encodeToString(RSAUtil.encrypt(iv.getBytes(), public3dsPayloadRSAKey), Base64.NO_WRAP);
            url = url.buildUpon()
                    .appendQueryParameter("transactionType", "TOKEN")
                    .appendQueryParameter("key", encryptedKey)
                    .appendQueryParameter("iv", encryptedIv)
                    .appendQueryParameter("payload", AESEncryptor.encrypt(aesKey, iv, payloadString))
                    .build();
            Intent intent = new Intent(activity, BrowserActivity.class);
            intent.putExtra("url", url.toString());
            intent.putExtra("mqttServer", mqttServer);
            final String topic = "merchant_portal/" + merchant.getMerchantId() + "/" + payment.getTransactionRef();
            intent.putExtra("topic", topic);
            activity.startActivity(intent);
            try {
                final MqttClient sampleClient = new MqttClient(mqttServer, UUID.randomUUID().toString(), new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                connOpts.setAutomaticReconnect(true);
                System.out.println("Connecting to broker: " + mqttServer);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                sampleClient.subscribe(topic, new IMqttMessageListener() {
                    @Override
                    public void messageArrived(String topic, final MqttMessage message) throws Exception {
                        // message Arrived!
                        System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
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
                                    transactionSuccessCallback.onSuccess(cardPaymentResponse);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    transactionFailureCallback.onError(new Exception(new String(message.getPayload())));
                                }
                            }
                        });
                    }
                });
            } catch (MqttException me) {
                me.printStackTrace();
            }
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public void makeMobileMoneyPayment(Mobile mobile, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(mobile, "mobile must not be null");
        try {
            MobilePaymentPayload mobilePaymentPayload = new MobilePaymentPayload(merchant, payment, customer, mobile);
            Disposable subscribe = retrofit.create(MobilePayment.class)
                    .mobilePayment(mobilePaymentPayload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MobilePaymentResponse>() {
                        @Override
                        public void accept(MobilePaymentResponse mobilePaymentResponse) {
                            Log.i(LOG_TAG, "Mobile payment succeeded, ref:\t" + mobilePaymentResponse.getTransactionRef());
                            transactionSuccessCallback.onSuccess(mobilePaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(LOG_TAG, "Mobile payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public void confirmMobilePayment(String orderId, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        try {
            Disposable subscribe = retrofit.create(MobilePayment.class)
                    .confirmMobilePayment(orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PaybillQueryResponse>() {
                        @Override
                        public void accept(PaybillQueryResponse mobilePaymentResponse) {
                            Log.i(LOG_TAG, "Mobile payment succeeded, ref:\t" + mobilePaymentResponse.getTransactionRef());
                            transactionSuccessCallback.onSuccess(mobilePaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(LOG_TAG, "Mobile payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public enum PaymentChannel {
        CARD("Card"), MOBILE("Mobile"), BANK("Bank"), PAYCODE("Verve Paycode"),PAYFROMPESALINK("Pay from Pesalink");
        public String value;

        PaymentChannel(String value) {
            this.value = value;
        }
    }

    public void makePesalinkPayment(Merchant merchant, Payment payment, Customer customer, final PesalinkSuccessCallback pesalinkSuccessCallback, final PesalinkFailureCallback pesalinkFailureCallback){
        try{
            PesalinkPaymentPayload pesalinkPaymentPayload = new PesalinkPaymentPayload(merchant,payment,customer);
            Disposable subscribe = retrofit.create(PesalinkPayment.class)
                    .pesalinkPayment(pesalinkPaymentPayload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PesalinkPaymentResponse>() {
                        @Override
                        public void accept(PesalinkPaymentResponse pesalinkPaymentResponse) throws Exception {
                            Log.i(LOG_TAG, "Pesalink payment code generation succeeded, code:\t" + pesalinkPaymentResponse.getExternalPaymentRef());
                            pesalinkSuccessCallback.onSuccess(pesalinkPaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            pesalinkFailureCallback.onError(throwable);
                        }
                    });
        }catch (Exception e){
            pesalinkFailureCallback.onError(e);
        }
    }

    public void confirmTransactionPayment(String transactionRef, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback){
        try{
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
        }catch (Exception e){
            transactionFailureCallback.onError(e);
        }
    }


    public static class Config {
        //All channels are enabled by default
        private List<PaymentChannel> channels = new LinkedList<>(Arrays.asList(PaymentChannel.class.getEnumConstants()));
        private List<CardToken> cardTokens = new ArrayList<>();

        public List<PaymentChannel> getChannels() {
            return channels;
        }

        public void setChannels(PaymentChannel... channels) {
            if (channels != null && channels.length > 0) {
                // Set enabled channels by first converting all channels varargs to set to remove duplicates
                this.channels = new ArrayList<>(new LinkedHashSet<>(Arrays.asList(channels)));
            }
        }

        public List<CardToken> getCardTokens() {
            return cardTokens;
        }

        public void setCardTokens(List<CardToken> cardTokens) {
            this.cardTokens.clear();
            this.cardTokens.addAll(cardTokens);
        }
    }
}
