package com.interswitchgroup.mobpaylib.ui.fragments.mobile;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.databinding.FragmentMobilePaymentBinding;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.ui.ImageSpinnerAdapter;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MobilePaymentFragment extends DaggerFragment {

    private static final String LOG_TAG = MobilePaymentFragment.class.getSimpleName();
    MobileVm mobileVm;
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
        mobileVm = ViewModelProviders.of(this, viewModelFactory).get(MobileVm.class);
        mobileVm.setPaymentVm(paymentVm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentMobilePaymentBinding fragmentMobilePaymentBinding = FragmentMobilePaymentBinding.inflate(inflater, container, false);
        fragmentMobilePaymentBinding.setMobileVm(mobileVm);
        fragmentMobilePaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = fragmentMobilePaymentBinding.spinner;
        final List<Pair<Mobile.Type, Integer>> namesAndImagesList = new ArrayList<>();
        namesAndImagesList.add(new Pair<>(Mobile.Type.MPESA, R.drawable.mpesa));
        namesAndImagesList.add(new Pair<>(Mobile.Type.EAZZYPAY, R.drawable.eazzypay));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "Spinner item was selected");
                String paybill = "";
                switch (namesAndImagesList.get(position).first) {
                    case EAZZYPAY:
                        paybill = MobPay.getMerchantConfigResponse().getConfig().getEquitelPaybill();
                        break;
                    case MPESA:
                        paybill = MobPay.getMerchantConfigResponse().getConfig().getMpesaPaybill();
                        break;
                }
                fragmentMobilePaymentBinding.mnoContentText.setText(paybill);
                MobilePaymentFragment.this.mobileVm.getMobile().setType(namesAndImagesList.get(position).first);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageSpinnerAdapter<Mobile.Type> imageSpinnerAdapter = new ImageSpinnerAdapter<>(MobilePaymentFragment.this.getActivity().getApplicationContext(), namesAndImagesList);
        spin.setAdapter(imageSpinnerAdapter);
        return fragmentMobilePaymentBinding.getRoot();
    }
}
