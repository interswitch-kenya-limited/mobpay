package com.interswitchgroup.mobpaylib.ui.fragments.card;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.databinding.FragmentCardPaymentBinding;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.utils.NetUtil;

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
        cardVm.setOnSuccess(new TransactionSuccessCallback() {
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.result_dialog, (ViewGroup) getView(), false);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.happy_face));
                title.setText(R.string.payment_successful_title);
                message.setText("Your transaction was completed successfully, your payment reference is " + transactionResponse.getTransactionReference());
                // TODO Find a way to dismiss the mobpay activity and using passed context from calling activity to launch the success dialog on it
                //                getActivity().finish();
                new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .create()
                        .show();
            }
        });
        cardVm.setOnFailure(new TransactionFailureCallback() {
            @Override
            public void onError(Throwable error) {
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.result_dialog, (ViewGroup) getView(), false);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sad_face));
                title.setText(R.string.payment_failed_title);
                ErrorWrapper errorWrapper = NetUtil.parseError(error);
                if (errorWrapper != null) {
                    message.setText(errorWrapper.getError().getStatusMessage());
                } else {
                    message.setText(error.getMessage());
                }
                new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCardPaymentBinding fragmentCardPaymentBinding = FragmentCardPaymentBinding.inflate(inflater, container, false);
        fragmentCardPaymentBinding.setModel(cardVm);
        return fragmentCardPaymentBinding.getRoot();
    }
}

