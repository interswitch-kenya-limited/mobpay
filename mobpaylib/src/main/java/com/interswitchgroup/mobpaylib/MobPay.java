package com.interswitchgroup.mobpaylib;

import android.util.Log;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.service.CardPayment;
import com.interswitchgroup.mobpaylib.di.DaggerWrapper;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.utils.NullChecker;

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
    public void pay(Merchant merchant, Payment payment, Customer customer, final TransactionSuccessCallback transactionSuccessCallback, final TransactionFailureCallback transactionFailureCallback) {
        NullChecker.checkNull(merchant, "merchant must not be null");
        NullChecker.checkNull(customer, "customer must not be null");
        NullChecker.checkNull(payment, "payment must not be null");
        NullChecker.checkNull(transactionSuccessCallback, "transactionSuccessCallback must not be null");
        NullChecker.checkNull(transactionFailureCallback, "transactionFailureCallback must not be null");
        CardPaymentPayload cardPaymentPayload = new CardPaymentPayload(payment.getAmount(), payment.getTransactionRef(), payment.getTerminalType(), payment.getTerminalId(), payment.getPaymentItem(), merchant.getMerchantId(), merchant.getAuthData(), payment.getCurrency(), merchant.getDomain());
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
        /*
        Launch ui
        collect card data
        build card payment payload
        send card payment request
        on response call appropriate transaction callback method
         */
    }
}
