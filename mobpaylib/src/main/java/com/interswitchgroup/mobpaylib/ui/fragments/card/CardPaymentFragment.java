package com.interswitchgroup.mobpaylib.ui.fragments.card;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.databinding.FragmentCardPaymentBinding;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardPaymentFragment extends DaggerFragment {

    CardVm cardVm;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private PaymentVm paymentVm;

    public CardPaymentFragment() {
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
        final FragmentCardPaymentBinding fragmentCardPaymentBinding = FragmentCardPaymentBinding.inflate(inflater, container, false);
        fragmentCardPaymentBinding.setCardVm(cardVm);
        // TODO Only show this radiogroup if merchant has passed some tokens to be used by this user.
        fragmentCardPaymentBinding.existingCard.setChecked(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentCardPaymentBinding.cardTokensSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_new));
        } else {
            fragmentCardPaymentBinding.cardTokensSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_classic));
        }
        fragmentCardPaymentBinding.cardSourceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.existing_card) {
                    fragmentCardPaymentBinding.cardNumber.setVisibility(View.GONE);
                    fragmentCardPaymentBinding.cardTokensSpinner.setVisibility(View.VISIBLE);
                    cardVm.setCardInfoSource(CardVm.CardInfoSource.TOKEN);
                } else if (checkedId == R.id.other_card) {
                    fragmentCardPaymentBinding.cardNumber.setVisibility(View.VISIBLE);
                    fragmentCardPaymentBinding.cardTokensSpinner.setVisibility(View.GONE);
                    cardVm.setCardInfoSource(CardVm.CardInfoSource.MANUAL_INPUT);
                }
            }
        });
        fragmentCardPaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        return fragmentCardPaymentBinding.getRoot();
    }

}

