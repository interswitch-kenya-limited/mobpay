package com.interswitchgroup.mobpaylib.ui.fragments.card;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.databinding.FragmentCardPaymentBinding;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.adapters.TokensSpinnerAdapter;

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
    private FragmentCardPaymentBinding fragmentCardPaymentBinding;

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
        fragmentCardPaymentBinding = FragmentCardPaymentBinding.inflate(inflater, container, false);
        fragmentCardPaymentBinding.setCardVm(cardVm);
        // TODO Only show this radiogroup if merchant has passed some tokens to be used by this user.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentCardPaymentBinding.cardTokensSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_new));
        } else {
            fragmentCardPaymentBinding.cardTokensSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_classic));
        }

        switch (MobPay.getMerchantConfig().getTokenizeStatus()) {
            case 0: //Always tokenize,
                cardVm.getCard().setTokenize(true);//Set card tokenize to true
                if (MobPay.getConfig().getCardTokens() != null && MobPay.getConfig().getCardTokens().size() > 0) {
                    initializeCardSourceRadioGroup();
                    initializeTokensSpinner();
                    fragmentCardPaymentBinding.savedCard.setChecked(true);
                }
                break;
            case 1: //Optional tokenization
                // Display the save card option, which when selected sets card tokenize to true.
                fragmentCardPaymentBinding.tokenizeCheckbox.setVisibility(View.VISIBLE);
                if (MobPay.getConfig().getCardTokens() != null && MobPay.getConfig().getCardTokens().size() > 0) {
                    initializeCardSourceRadioGroup();
                    initializeTokensSpinner();
                }
                fragmentCardPaymentBinding.savedCard.setChecked(true);
                break;
            case 2: //Never tokenize, hide everything, set card tokenization to false, and follow normal flow
                cardVm.getCard().setTokenize(false);//Set card tokenize to true
                fragmentCardPaymentBinding.savedCard.setChecked(false);
                fragmentCardPaymentBinding.newCard.setChecked(true);
                fragmentCardPaymentBinding.cardTokensSpinner.setVisibility(View.GONE);
                fragmentCardPaymentBinding.cardSourceRadioGroup.setVisibility(View.GONE);
                break;
        }

        fragmentCardPaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        return fragmentCardPaymentBinding.getRoot();
    }

    private void initializeCardSourceRadioGroup() {
        fragmentCardPaymentBinding.cardSourceRadioGroup.setVisibility(View.VISIBLE);
        fragmentCardPaymentBinding.cardSourceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.saved_card) {
                    fragmentCardPaymentBinding.cardNumber.setVisibility(View.GONE);
                    fragmentCardPaymentBinding.cardTokensSpinner.setVisibility(View.VISIBLE);
                    cardVm.setCardInfoSource(CardVm.CardInfoSource.TOKEN);
                    cardVm.getCard().setTokenize(false);
                    fragmentCardPaymentBinding.tokenizeCheckbox.setVisibility(View.GONE);
                    fragmentCardPaymentBinding.expiryDate.setEnabled(false);
                    String[] expiry = MobPay.getConfig().getCardTokens().get(fragmentCardPaymentBinding.cardTokensSpinner.getSelectedItemPosition()).getExpiry().split("(?<=\\G.{2})");
                    fragmentCardPaymentBinding.expiryDate.setText(expiry[1] + expiry[0]);
                } else if (checkedId == R.id.new_card) {
                    if (MobPay.getMerchantConfig().getTokenizeStatus() == 1) {// When tokenization is optional show checkbox
                        fragmentCardPaymentBinding.tokenizeCheckbox.setVisibility(View.VISIBLE);
                    }
                    fragmentCardPaymentBinding.expiryDate.setEnabled(true);
                    fragmentCardPaymentBinding.expiryDate.setText("");// Maybe I should save the previous manually entered value and re-set it here
                    fragmentCardPaymentBinding.cardNumber.setVisibility(View.VISIBLE);
                    fragmentCardPaymentBinding.cardTokensSpinner.setVisibility(View.GONE);
                    cardVm.setCardInfoSource(CardVm.CardInfoSource.MANUAL_INPUT);
                }
            }
        });
    }

    private void initializeTokensSpinner() {
        TokensSpinnerAdapter<CardToken> imageSpinnerAdapter = new TokensSpinnerAdapter<>(getContext(), MobPay.getConfig().getCardTokens());
        fragmentCardPaymentBinding.cardTokensSpinner.setAdapter(imageSpinnerAdapter);
        fragmentCardPaymentBinding.cardTokensSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CardToken selectedCardToken = MobPay.getConfig().getCardTokens().get(position);
                String[] expiryParts = selectedCardToken.getExpiry().split("(?<=\\G.{2})");
                fragmentCardPaymentBinding.expiryDate.setText(expiryParts[1] + expiryParts[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}

