package com.interswitchgroup.mobpaylib.ui.fragments.card;

import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.model.Card;

import javax.inject.Inject;

public class CardVm extends ViewModel {
    private PaymentVm paymentVm;
    private Card card;
    private CardInfoSource cardInfoSource;

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

    public void setCardInfoSource(CardInfoSource cardInfoSource) {
        this.cardInfoSource = cardInfoSource;
    }

    public void makePayment() {
        paymentVm.makeCardPayment(card);
    }

    public enum CardInfoSource {MANUAL_INPUT, TOKEN}
}
