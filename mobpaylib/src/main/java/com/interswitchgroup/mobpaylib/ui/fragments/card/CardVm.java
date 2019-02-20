package com.interswitchgroup.mobpaylib.ui.fragments.card;

import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;

import javax.inject.Inject;

public class CardVm extends ViewModel {
    private String LOG_TAG = this.getClass().getSimpleName();
    private PaymentVm paymentVm;
    private Card card;
    private TransactionSuccessCallback onSuccess;
    private TransactionFailureCallback onFailure;

    @Inject
    public CardVm() {
        this.card = new Card();
    }

    public PaymentVm getPaymentVm() {
        return paymentVm;
    }

    public void setPaymentVm(PaymentVm paymentVm) {
        this.paymentVm = paymentVm;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void makePayment() {
        paymentVm.makeCardPayment(card);
    }
}
