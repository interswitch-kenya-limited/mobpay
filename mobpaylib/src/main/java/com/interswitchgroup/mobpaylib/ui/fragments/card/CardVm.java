package com.interswitchgroup.mobpaylib.ui.fragments.card;

import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.model.Card;

import javax.inject.Inject;

public class CardVm extends ViewModel {
    private Card card;
    @Inject
    public CardVm() {
        card = new Card();
    }

    public Card getCard() {
        return card;
    }

    public void doPayment() {
//        login(new LoginCredentials(username.getValue(), password.getValue()));
    }
}
