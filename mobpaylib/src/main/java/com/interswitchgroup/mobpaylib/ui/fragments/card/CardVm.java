package com.interswitchgroup.mobpaylib.ui.fragments.card;

import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

import javax.inject.Inject;

public class CardVm extends ViewModel {
    private MobPay mobPay;
    private Card card;
    private Merchant merchant;
    private Payment payment;
    private Customer customer;
    private String LOG_TAG = this.getClass().getSimpleName();
    private TransactionSuccessCallback onSuccess;
    private TransactionFailureCallback onFailure;

    @Inject
    public CardVm() {
        card = new Card();
    }

    public void setMobPay(MobPay mobPay) {
        this.mobPay = mobPay;
    }

    public Card getCard() {
        return card;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOnSuccess(TransactionSuccessCallback onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void setOnFailure(TransactionFailureCallback onFailure) {
        this.onFailure = onFailure;
    }

    public void makePayment() {
        mobPay.makeCardPayment(
                card,
                merchant,
                payment,
                customer,
                onSuccess,
                onFailure);
    }
}
