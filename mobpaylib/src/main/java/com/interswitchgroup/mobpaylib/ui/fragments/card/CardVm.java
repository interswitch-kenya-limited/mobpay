package com.interswitchgroup.mobpaylib.ui.fragments.card;

import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.CardToken;

import javax.inject.Inject;

public class CardVm extends ViewModel {
    private PaymentVm paymentVm;
    private Card card;
    private CardToken cardToken;
    private CardInfoSource cardInfoSource = CardInfoSource.MANUAL_INPUT;

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

    public void setCardToken(CardToken cardToken) {
        this.cardToken = cardToken;
    }

    public void makePayment() {
        switch (cardInfoSource) {
            case TOKEN:
                cardToken.setCvv(card.getCvv());
                cardToken.setExpiry(card.getFullExpiry());
                paymentVm.makeCardTokenPayment(cardToken);
                break;
            case MANUAL_INPUT:
                paymentVm.makeCardPayment(card);
                break;
        }
    }

    public enum CardInfoSource {MANUAL_INPUT, TOKEN}
}
