package com.interswitchgroup.mobpaylib.ui.fragments.bank;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import com.interswitchgroup.mobpaylib.databinding.FragmentBankPaymentBinding;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.adapters.EnumPairSpinnerAdapter;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;
import com.interswitchgroup.mobpaylib.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.interswitchgroup.mobpaylib.model.Mobile.Type.EAZZYPAY;
import static com.interswitchgroup.mobpaylib.model.Mobile.Type.MPESA;

public class BankPaymentFragment extends Fragment {

    private static final String LOG_TAG = BankPaymentFragment.class.getSimpleName();
    private final List<Pair<Mobile.Type, Integer>> providereEnumLogoPairs = new ArrayList<>();
    BankVm bankVm;
    private PaymentVm paymentVm;
    private FragmentBankPaymentBinding fragmentBankPaymentBinding;


    public BankPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentVm = ((MobPayActivity) getActivity()).paymentVm;
        bankVm = new BankVm();
        bankVm.setMobile(new Mobile());// Reset the model so that form is cleared
        bankVm.setPaymentVm(paymentVm);
        bankVm.setExpressTransactionFailureCallback(new TransactionFailureCallback() {
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
        bankVm.setPaybillTransactionFailureCallback(new TransactionFailureCallback() {
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
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Trying again", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bankVm.makePayment();
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
        fragmentBankPaymentBinding = FragmentBankPaymentBinding.inflate(inflater, container, false);
        fragmentBankPaymentBinding.setBankVm(bankVm);
        fragmentBankPaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentBankPaymentBinding.ussdActionsSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_new));
        } else {
            fragmentBankPaymentBinding.ussdActionsSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_classic));
        }
        providereEnumLogoPairs.clear();
        providereEnumLogoPairs.add(new Pair<>(MPESA, R.drawable.mpesa));
        providereEnumLogoPairs.add(new Pair<>(EAZZYPAY, R.drawable.eazzypay));
        fragmentBankPaymentBinding.ussdActionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BankPaymentFragment.this.bankVm.getMobile().setType(providereEnumLogoPairs.get(position).first);
                switch (providereEnumLogoPairs.get(position).first) {
                    case EAZZYPAY:
                        fragmentBankPaymentBinding.mobile.setHint("0763 000 000");
                        break;
                    case MPESA:
                        fragmentBankPaymentBinding.mobile.setHint("0712 000 000");
                        break;
                }
                changeMobileValidityBackground();
                if (bankVm.getPaymentMethod().equalsIgnoreCase(BankVm.PAYBIll)) {
                    // Button should ignore mobile validity if method is express
                    fragmentBankPaymentBinding.payButton.setEnabled(true);
                    bankVm.getMobile().setMobileFullyValid(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentBankPaymentBinding.mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeMobileValidityBackground();
            }
        });
        EnumPairSpinnerAdapter<Mobile.Type> enumPairSpinnerAdapter = new EnumPairSpinnerAdapter<>(getContext(), providereEnumLogoPairs);
        fragmentBankPaymentBinding.ussdActionsSpinner.setAdapter(enumPairSpinnerAdapter);
        return fragmentBankPaymentBinding.getRoot();
    }

    private void changeMobileValidityBackground() {
        if (bankVm.getMobile().isMobileFullyValid()) {
            fragmentBankPaymentBinding.mobile.setBackground(getResources().getDrawable(R.drawable.textbox_valid));
        } else if (bankVm.getMobile().isMobilePartiallyValid()) {
            fragmentBankPaymentBinding.mobile.setBackground(getResources().getDrawable(R.drawable.textbox_probably_valid));
        } else {
            if (fragmentBankPaymentBinding.mobile.getText().length() > 3) {
                fragmentBankPaymentBinding.mobile.setBackground(getResources().getDrawable(R.drawable.textbox_invalid));
            } else {
                fragmentBankPaymentBinding.mobile.setBackground(getResources().getDrawable(R.drawable.textbox_neutral));
            }
        }
    }

}
