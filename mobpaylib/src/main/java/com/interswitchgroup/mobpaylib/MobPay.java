package com.interswitchgroup.mobpaylib;

import android.util.Log;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.service.CardPayment;
import com.interswitchgroup.mobpaylib.di.DaggerWrapper;
import com.interswitchgroup.mobpaylib.model.Invoice;
import com.interswitchgroup.mobpaylib.model.Transaction;
import com.interswitchgroup.mobpaylib.utils.NullChecker;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class MobPay {
    private final String LOG_TAG = this.getClass().getSimpleName();
    Retrofit retrofit;

    public MobPay() {
        DaggerWrapper.getComponent().inject(this);
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
     *
     * @param invoice - The invoice to be paid
     */
    public void pay(Invoice invoice, TransactionCallback transactionCallback) {
        NullChecker.checkNull(invoice, "Invoice must not be null");
        NullChecker.checkNull(transactionCallback, "Transaction callback must not be null");
        Disposable subscribe = retrofit.create(CardPayment.class)
                .merchantCardPayment(new CardPaymentPayload("amount", "transactionRef", "terminalType", "terminalId", "paymentItem", "merchantId", "authData", "currency", "domain"))
                .subscribe(new Consumer<CardPaymentResponse>() {
                    @Override
                    public void accept(CardPaymentResponse cardPaymentResponse) throws Exception {
                        Log.i(LOG_TAG, "call successful" + cardPaymentResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "call failed" + throwable.getMessage());
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

    public interface TransactionCallback {
        void onSuccess(Transaction transaction);

        void onUserCancel();

        void onError(Throwable error, Transaction transaction);
    }
}
