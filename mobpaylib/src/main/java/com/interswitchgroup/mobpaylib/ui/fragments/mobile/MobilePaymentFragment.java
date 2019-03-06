package com.interswitchgroup.mobpaylib.ui.fragments.mobile;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import com.interswitchgroup.mobpaylib.databinding.FragmentMobilePaymentBinding;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.ui.ImageSpinnerAdapter;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;
import com.interswitchgroup.mobpaylib.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.interswitchgroup.mobpaylib.model.Mobile.Type.EAZZYPAY;
import static com.interswitchgroup.mobpaylib.model.Mobile.Type.MPESA;

public class MobilePaymentFragment extends DaggerFragment {

    private static final String LOG_TAG = MobilePaymentFragment.class.getSimpleName();
    MobileVm mobileVm;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private PaymentVm paymentVm;
    private final List<Pair<Mobile.Type, Integer>> namesAndImagesList = new ArrayList<>();


    public MobilePaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentVm = ViewModelProviders.of(this, viewModelFactory).get(PaymentVm.class);
        mobileVm = ViewModelProviders.of(this, viewModelFactory).get(MobileVm.class);
        mobileVm.setPaymentVm(paymentVm);
        mobileVm.setExpressTransactionFailureCallback(new TransactionFailureCallback() {
            @Override
            public void onError(final Throwable error) {
                paymentVm.getLoading().set(false);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.result_dialog, (ViewGroup) getActivity().getWindow().getDecorView().getRootView(), false);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sad_face));
                title.setText(R.string.payment_failed_title);
                ErrorWrapper errorWrapper = NetUtil.parseError(error);
                if (errorWrapper != null) {
                    message.setText(errorWrapper.getError().getCode() + " " + errorWrapper.getError().getMessage() + " " + errorWrapper.getError().getStatusMessage());
                } else {
                    message.setText(error.getMessage());
                }
                message.setText(R.string.failed_payment_message);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Trying again", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mobileVm.makePayment();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        paymentVm.getMobPay().getTransactionFailureCallback().onError(error);
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });
        mobileVm.setPaybillTransactionFailureCallback(new TransactionFailureCallback() {
            @Override
            public void onError(final Throwable error) {
                paymentVm.getLoading().set(false);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.result_dialog, (ViewGroup) getActivity().getWindow().getDecorView().getRootView(), false);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sad_face));
                title.setText(R.string.payment_failed_title);
                ErrorWrapper errorWrapper = NetUtil.parseError(error);
                if (errorWrapper != null) {
                    message.setText(errorWrapper.getError().getCode() + " " + errorWrapper.getError().getMessage() + " " + errorWrapper.getError().getStatusMessage());
                } else {
                    message.setText(error.getMessage());
                }
                message.setText(R.string.failed_payment_message);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Trying again", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mobileVm.makePayment();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        paymentVm.getMobPay().getTransactionFailureCallback().onError(error);
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });
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
        final Spinner spin = fragmentMobilePaymentBinding.spinner;
        namesAndImagesList.add(new Pair<>(MPESA, R.drawable.mpesa));
        namesAndImagesList.add(new Pair<>(EAZZYPAY, R.drawable.eazzypay));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t2 = getInstructionText(position, mobileVm.getPaymentMethod().equalsIgnoreCase(MobileVm.EXPRESS));
                fragmentMobilePaymentBinding.mnoContentText.setText(t2);
                MobilePaymentFragment.this.mobileVm.getMobile().setType(namesAndImagesList.get(position).first);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentMobilePaymentBinding.payMethodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String t2 = "";
                if (checkedId == R.id.express) {
                    t2 = getInstructionText(spin.getSelectedItemPosition(), true);
                    fragmentMobilePaymentBinding.mobile.setVisibility(View.VISIBLE);
                    fragmentMobilePaymentBinding.payButton.setText("Pay " + mobileVm.getPaymentVm().getPayment().getCurrency() + " " + mobileVm.getPaymentVm().getPayment().getAmountString());
                    fragmentMobilePaymentBinding.payButton.setEnabled(mobileVm.getMobile().valid);
                } else if (checkedId == R.id.paybill) {
                    t2 = getInstructionText(spin.getSelectedItemPosition(), false);
                    fragmentMobilePaymentBinding.mobile.setVisibility(View.GONE);
                    fragmentMobilePaymentBinding.payButton.setText("Confirm Payment");
                    fragmentMobilePaymentBinding.payButton.setEnabled(true);
                }
                fragmentMobilePaymentBinding.mnoContentText.setText(t2);
            }
        });
        ImageSpinnerAdapter<Mobile.Type> imageSpinnerAdapter = new ImageSpinnerAdapter<>(getContext(), namesAndImagesList);
        spin.setAdapter(imageSpinnerAdapter);
        return fragmentMobilePaymentBinding.getRoot();
    }

    private String getInstructionText(int provider, boolean express) {
        String t2 = "";
        if (express) {
            String mno = namesAndImagesList.get(provider).first.value;
//                String t2 = getString(R.string.eazzypay_manual_payment_instructions);
            t2 = getString(R.string.push_payment_instructions);
            t2 = String.format(t2, mno);
        } else {
            //
            switch (namesAndImagesList.get(provider).first) {
                case MPESA:
                    t2 = getString(R.string.mpesa_manual_payment_instructions);
                    break;
                case EAZZYPAY:
                    t2 = getString(R.string.eazzypay_manual_payment_instructions);
                    break;
            }
            t2 = String.format(t2, MobPay.getMerchantConfigResponse().getConfig().getEquitelPaybill(), paymentVm.getPayment().getOrderId(), paymentVm.getPayment().getCurrency() + " " + paymentVm.getPayment().getAmountString());
        }
        return t2;
    }
}
