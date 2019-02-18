package com.interswitchgroup.mobpaylib.ui.fragments.card;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interswitchgroup.mobpaylib.databinding.FragmentCardPaymentBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardPaymentFragment extends DaggerFragment {

    CardVm cardVm;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardVm = ViewModelProviders.of(this, viewModelFactory).get(CardVm.class);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CardPaymentFragment newInstance() {
        return new CardPaymentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCardPaymentBinding fragmentCardPaymentBinding = FragmentCardPaymentBinding.inflate(inflater, container, false);
        return fragmentCardPaymentBinding.getRoot();
    }
}

