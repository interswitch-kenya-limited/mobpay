package com.interswitchgroup.mobpaylib.ui.fragments.mobile;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interswitchgroup.mobpaylib.databinding.FragmentMobilePaymentBinding;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.fragments.card.CardVm;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MobilePaymentFragment extends DaggerFragment {

    CardVm cardVm;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private PaymentVm paymentVm;

    public MobilePaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentVm = ViewModelProviders.of(this, viewModelFactory).get(PaymentVm.class);
        cardVm = ViewModelProviders.of(this, viewModelFactory).get(CardVm.class);
        cardVm.setPaymentVm(paymentVm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMobilePaymentBinding fragmentCardPaymentBinding = FragmentMobilePaymentBinding.inflate(inflater, container, false);
        fragmentCardPaymentBinding.setCardVm(cardVm);
        fragmentCardPaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        return fragmentCardPaymentBinding.getRoot();
    }
}
