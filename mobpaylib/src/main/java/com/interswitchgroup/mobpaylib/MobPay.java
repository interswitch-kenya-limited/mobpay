package com.interswitchgroup.mobpaylib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.cardinalcommerce.cardinalmobilesdk.Cardinal;
import com.cardinalcommerce.cardinalmobilesdk.models.response.ValidateResponse;
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService;
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver;
import com.cardinalcommerce.shared.models.enums.DirectoryServerID;
import com.cardinalcommerce.shared.models.parameters.CardinalConfigurationParameters;
import com.cardinalcommerce.shared.models.parameters.CardinalEnvironment;
import com.cardinalcommerce.shared.models.parameters.CardinalRenderType;
import com.cardinalcommerce.shared.models.parameters.CardinalUiType;
import com.cardinalcommerce.shared.userinterfaces.UiCustomization;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.MerchantConfigResponse;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.PaybillQueryResponse;
import com.interswitchgroup.mobpaylib.api.model.ThreeDSecureAPIResponse;
import com.interswitchgroup.mobpaylib.api.service.CardPayment;
import com.interswitchgroup.mobpaylib.api.service.MerchantConfig;
import com.interswitchgroup.mobpaylib.api.service.MobilePayment;
import com.interswitchgroup.mobpaylib.api.service.ThreeDSecure;
import com.interswitchgroup.mobpaylib.di.DaggerWrapper;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.utils.NullChecker;
import com.interswitchgroup.mobpaylib.utils.RSAUtil;

import org.json.JSONArray;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MobPay implements Serializable {
    private static MobPay singletonMobPayInstance;
    private static final String LOG_TAG = MobPay.class.getSimpleName();
    private String clientId;
    private String clientSecret;
    private static Cardinal cardinal = Cardinal.getInstance();
    private Retrofit retrofit;
    private TransactionFailureCallback transactionFailureCallback;
    private TransactionSuccessCallback transactionSuccessCallback;
    private MerchantConfigResponse.Config merchantConfig;
    private static Config config;
    private Activity activity;

    private MobPay() {
    }

    public static MobPay getInstance(Activity activity,String clientId, String clientSecret, Config config)  throws Exception{
        if (singletonMobPayInstance == null) {
            singletonMobPayInstance = new MobPay();
            DaggerWrapper.getComponent(clientId, clientSecret).inject(singletonMobPayInstance);
            singletonMobPayInstance.clientId = clientId;
            singletonMobPayInstance.clientSecret = clientSecret;
            CardinalConfigurationParameters cardinalConfigurationParameters = new CardinalConfigurationParameters();
            cardinalConfigurationParameters.setEnvironment(CardinalEnvironment.STAGING);
            cardinalConfigurationParameters.setTimeout(8000);
            JSONArray rType = new JSONArray();
            rType.put(CardinalRenderType.OTP);
            rType.put(CardinalRenderType.SINGLE_SELECT);
            rType.put(CardinalRenderType.MULTI_SELECT);
            rType.put(CardinalRenderType.OOB);
            rType.put(CardinalRenderType.HTML);
            cardinalConfigurationParameters.setRenderType(rType);

            cardinalConfigurationParameters.setUiType(CardinalUiType.BOTH);

            UiCustomization yourUICustomizationObject = new UiCustomization();
            cardinalConfigurationParameters.setUICustomization(yourUICustomizationObject);
            singletonMobPayInstance.activity = activity;
            cardinal.configure(activity.getApplicationContext(), cardinalConfigurationParameters);
        }

        if (singletonMobPayInstance.getMerchantConfig() == null) {
            singletonMobPayInstance.initializeMerchantConfig();
        }

        // If enabled channels wasmakeMobileMoneyPayment explicitly passed, override default enabled channels
        if (config != null) {
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
        try {
            // TODO The Modulus and Public Exponent will be supplied by Interswitch. Please ask for one
            PublicKey publicKey = RSAUtil.getPublicKey("9c7b3ba621a26c4b02f48cfc07ef6ee0aed8e12b4bd11c5cc0abf80d5206be69e1891e60fc88e2d565e2fabe4d0cf630e318a6c721c3ded718d0c530cdf050387ad0a30a336899bbda877d0ec7c7c3ffe693988bfae0ffbab71b25468c7814924f022cb5fda36e0d2c30a7161fa1c6fb5fbd7d05adbef7e68d48f8b6c5f511827c4b1c5ed15b6f20555affc4d0857ef7ab2b5c18ba22bea5d3a79bd1834badb5878d8c7a4b19da20c1f62340b1f7fbf01d2f2e97c9714a9df376ac0ea58072b2b77aeb7872b54a89667519de44d0fc73540beeaec4cb778a45eebfbefe2d817a8a8319b2bc6d9fa714f5289ec7c0dbc43496d71cf2a642cb679b0fc4072fd2cf", "010001");
            String authData = RSAUtil.getAuthDataMerchant(publicKey, card.getPan(), card.getCvv(), card.getExpiryYear() + card.getExpiryMonth(), card.isTokenize() ? 1 : 0, "D");
            final CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(merchant, payment, customer, authData);
            Disposable subscribe = retrofit.create(ThreeDSecure.class)
                    .cardInitialize(cardPaymentPayload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ThreeDSecureAPIResponse>() {
                        @Override
                        public void accept(ThreeDSecureAPIResponse cardInitializeResponse) throws Exception {
                            final ThreeDSecureAPIResponse internalCardInitializeResponse = cardInitializeResponse;
                            cardinal.init(cardInitializeResponse.getJwt(),
                                    new CardinalInitService() {
                                        /**
                                         * You may have your Submit button disabled on page load. Once you are setup
                                         * for CCA, you may then enable it. This will prevent users from submitting
                                         * their order before CCA is ready.
                                         */
                                        @Override
                                        public void onSetupCompleted(final String consumerSessionId) {
                                            Disposable subscribe1 = retrofit.create(ThreeDSecure.class)
                                                    .checkEnrollAction(internalCardInitializeResponse, consumerSessionId)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ThreeDSecureAPIResponse>() {
                                                        @Override
                                                        public void accept(ThreeDSecureAPIResponse checkEnrollResponse) {
                                                            String cardPaymentPayloadJson = cardPaymentPayload.toString();
                                                            cardinal.cca_continue("GEPGTZ0001oid_12121", Base64.encodeToString("{\"amount\":\"993\",\"currencyCode\":\"KES\",\"orderNumber\":\"GEPGTZ0001oid_12121\"}".getBytes(), Base64.NO_WRAP), DirectoryServerID.VISA01, activity, new CardinalValidateReceiver() {
                                                                @Override
                                                                public void onValidated(Context context, ValidateResponse validateResponse, String s) {
                                                                    transactionFailureCallback.onError(new Exception(validateResponse.errorDescription));
                                                                }
                                                            });
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) {

                                                        }
                                                    });
                                        }

                                        /**
                                         * If there was an error with setup, cardinal will call this function with
                                         * validate response and empty serverJWT
                                         *
                                         * @param validateResponse
                                         * @param serverJwt        will be an empty
                                         */
                                        @Override
                                        public void onValidated(ValidateResponse validateResponse, String serverJwt) {
                                            transactionFailureCallback.onError(new Exception(validateResponse.errorDescription));
                                        }
                                    });
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(LOG_TAG, "Card payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }

    public void makeCardTokenPayment(CardToken cardToken, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(cardToken, "cardToken must not be null");
        try {
            // TODO The Modulus and Public Exponent will be supplied by Interswitch. Please ask for one
            PublicKey publicKey = RSAUtil.getPublicKey("9c7b3ba621a26c4b02f48cfc07ef6ee0aed8e12b4bd11c5cc0abf80d5206be69e1891e60fc88e2d565e2fabe4d0cf630e318a6c721c3ded718d0c530cdf050387ad0a30a336899bbda877d0ec7c7c3ffe693988bfae0ffbab71b25468c7814924f022cb5fda36e0d2c30a7161fa1c6fb5fbd7d05adbef7e68d48f8b6c5f511827c4b1c5ed15b6f20555affc4d0857ef7ab2b5c18ba22bea5d3a79bd1834badb5878d8c7a4b19da20c1f62340b1f7fbf01d2f2e97c9714a9df376ac0ea58072b2b77aeb7872b54a89667519de44d0fc73540beeaec4cb778a45eebfbefe2d817a8a8319b2bc6d9fa714f5289ec7c0dbc43496d71cf2a642cb679b0fc4072fd2cf", "010001");
            String authData = RSAUtil.getAuthDataMerchant(publicKey, cardToken.getToken(), cardToken.getCvv(), cardToken.getExpiry().replaceAll("[^\\d]", ""), 0, ",");
            CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(merchant, payment, customer, authData);
            Disposable subscribe = retrofit.create(CardPayment.class)
                    .merchantCardTokenPayment(cardPaymentPayload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CardPaymentResponse>() {
                        @Override
                        public void accept(CardPaymentResponse cardPaymentResponse) {
                            Log.i(LOG_TAG, "Card token payment succeeded, ref:\t" + cardPaymentResponse.getTransactionRef());
                            transactionSuccessCallback.onSuccess(cardPaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.e(LOG_TAG, "Card token payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
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
        CARD("Card"), MOBILE("Mobile"), BANK("Bank"), PAYCODE("Verve Paycode");
        public String value;

        PaymentChannel(String value) {
            this.value = value;
        }
    }

    public static class Config {
        private List<PaymentChannel> channels = Arrays.asList(PaymentChannel.class.getEnumConstants());
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
