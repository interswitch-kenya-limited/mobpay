package com.interswitchgroup.mobpaylib.ui.fragments.card;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.databinding.FragmentCardPaymentBinding;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardPaymentFragment extends DaggerFragment {

    CardVm cardVm;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private String clientId;
    private String clientSecret;
    private Customer customer;
    private Merchant merchant;
    private Payment payment;

    public CardPaymentFragment(Merchant merchant, Customer customer, Payment payment, String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.customer = customer;
        this.merchant = merchant;
        this.payment = payment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardVm = ViewModelProviders.of(this, viewModelFactory).get(CardVm.class);
        cardVm.setMobPay(new MobPay(this.clientId, this.clientSecret));
        cardVm.setCustomer(this.customer);
        cardVm.setMerchant(this.merchant);
        cardVm.setPayment(this.payment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCardPaymentBinding fragmentCardPaymentBinding = FragmentCardPaymentBinding.inflate(inflater, container, false);
        fragmentCardPaymentBinding.setModel(cardVm);
        return fragmentCardPaymentBinding.getRoot();
    }
}

