package com.interswitchgroup.mobpaylib;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.service.CardPayment;
import com.interswitchgroup.mobpaylib.di.DaggerWrapper;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.utils.NullChecker;
import com.interswitchgroup.mobpaylib.utils.RSAUtil;

import java.security.PublicKey;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MobPay {
    private final String LOG_TAG = this.getClass().getSimpleName();
    Retrofit retrofit;

    public MobPay(String clientId, String clientSecret) {
        DaggerWrapper.getComponent(clientId, clientSecret).inject(this);
    }

    @Inject
    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
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
    public void pay(Activity context, Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(context, "Activity context must not be null");
        NullChecker.checkNull(merchant, "merchant must not be null");
        NullChecker.checkNull(customer, "customer must not be null");
        NullChecker.checkNull(payment, "payment must not be null");
        NullChecker.checkNull(transactionSuccessCallback, "transactionSuccessCallback must not be null");
        NullChecker.checkNull(transactionFailureCallback, "transactionFailureCallback must not be null");
        Intent intent = new Intent(context, MobPayActivity.class);
        context.startActivity(intent);
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
            String authData = RSAUtil.getAuthDataMerchant(publicKey, card);
            CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(merchant, payment, customer, authData);
            Disposable subscribe = retrofit.create(CardPayment.class)
                    .merchantCardPayment(cardPaymentPayload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CardPaymentResponse>() {
                        @Override
                        public void accept(CardPaymentResponse cardPaymentResponse) throws Exception {
                            Log.i(LOG_TAG, "Card payment succeeded, ref:\t" + cardPaymentResponse.getTransactionRef());
                            transactionSuccessCallback.onSuccess(cardPaymentResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(LOG_TAG, "Card payment failed, reason:\t" + throwable.getMessage());
                            transactionFailureCallback.onError(throwable);
                        }
                    });
        } catch (Exception e) {
            transactionFailureCallback.onError(e);
        }
    }
}
