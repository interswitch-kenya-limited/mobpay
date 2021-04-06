package com.interswitchgroup.mobpaylib.ui.fragments.payfrompesalink;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentResponse;
import com.interswitchgroup.mobpaylib.databinding.FragmentPayFromPesalinkPaymentBinding;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkSuccessCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.PayFromPesalink;
import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.adapters.EnumPairSpinnerAdapter;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;
import com.interswitchgroup.mobpaylib.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.COOPERATIVEBANK;
import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.IMBANK;
import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.KCBBANK;
import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.NCBA;
import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.STANBIC;
import static com.interswitchgroup.mobpaylib.model.PayFromPesalink.Bank.STANDARDCHARTERED;


public class PayFromPesalinkFragment extends Fragment {

    private static final String LOG_TAG = PayFromPesalinkFragment.class.getSimpleName();
    PayFromPesalinkVM payFromPesalinkVM;
    PayFromPesalink payFromPesalink;
    private PaymentVm paymentVm;
    private final List<Pair<PayFromPesalink.Bank,Integer>> bankNameAndLogoPairs = new ArrayList<>();
    private FragmentPayFromPesalinkPaymentBinding fragmentPayFromPesalinkPaymentBinding;


    public PayFromPesalinkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentVm = ((MobPayActivity) getActivity()).paymentVm;





        //get the code
        //set loading to true
        paymentVm.getLoading().set(true);
        //Refresh the pesalink fragment
            paymentVm.getExternalTransactionReference(paymentVm.getPayment().getTransactionRef(), new PesalinkSuccessCallback() {
                @Override
                public void onSuccess(PesalinkPaymentResponse pesalinkResponse) {
                    System.out.println(pesalinkResponse.getExternalPaymentRef());
                    //set the pesalink external ref then remove the loader
                    payFromPesalinkVM.setExternalTrasactionReference(pesalinkResponse.getExternalPaymentRef());
                    //remove the loader
                    paymentVm.getLoading().set(false);
                    //Edit text
                    String t2 = getInstructionText();
                    fragmentPayFromPesalinkPaymentBinding.mnoContentText.setText(t2);

                }
            }, new PesalinkFailureCallback() {
                @Override
                public void onError(Throwable error) {
                    System.out.println(error.getLocalizedMessage());
                }
            });


        payFromPesalinkVM = new PayFromPesalinkVM();
        payFromPesalink = new PayFromPesalink(NCBA);
        payFromPesalinkVM.setPayFromPesalink(payFromPesalink);// Reset the model so that form is cleared
        payFromPesalinkVM.setPaymentVm(paymentVm);
        payFromPesalinkVM.setTransactionFailureCallback(new TransactionFailureCallback() {
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
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Try Paybill", new DialogInterface.OnClickListener() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentPayFromPesalinkPaymentBinding = FragmentPayFromPesalinkPaymentBinding.inflate(inflater, container, false);
        fragmentPayFromPesalinkPaymentBinding.setPayFromPesalinkVM(payFromPesalinkVM);
        fragmentPayFromPesalinkPaymentBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MobPayActivity) getActivity()).quit();
            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentPayFromPesalinkPaymentBinding.spinner.setBackground(getResources().getDrawable(R.drawable.spinner_new));
        } else {
            fragmentPayFromPesalinkPaymentBinding.spinner.setBackground(getResources().getDrawable(R.drawable.spinner_classic));
        }
        bankNameAndLogoPairs.clear();
        bankNameAndLogoPairs.add(new Pair<>(NCBA, R.drawable.ncba_logo));
        bankNameAndLogoPairs.add(new Pair<>(STANBIC, R.drawable.stanbic_bank));
        bankNameAndLogoPairs.add(new Pair<>(STANDARDCHARTERED, R.drawable.standard_chartered_logo));
        bankNameAndLogoPairs.add(new Pair<>(COOPERATIVEBANK, R.drawable.cooperative_logo));
        bankNameAndLogoPairs.add(new Pair<>(IMBANK, R.drawable.im_logo));
        bankNameAndLogoPairs.add(new Pair<>(KCBBANK, R.drawable.kcb_logo));


        EnumPairSpinnerAdapter<PayFromPesalink.Bank> enumPairSpinnerAdapter = new EnumPairSpinnerAdapter<>(getContext(), bankNameAndLogoPairs);
        fragmentPayFromPesalinkPaymentBinding.spinner.setAdapter(enumPairSpinnerAdapter);
        fragmentPayFromPesalinkPaymentBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (bankNameAndLogoPairs.get(position).first.getValue()){
                    case "NCBA":
                        payFromPesalink.setBank(NCBA);
                        break;
                    case "STANBIC":
                        payFromPesalink.setBank(STANBIC);
                        break;
                    case "STANDARD CHARTERED":
                        payFromPesalink.setBank(STANDARDCHARTERED);
                        break;
                    case "COOPERATIVE BANK":
                        payFromPesalink.setBank(COOPERATIVEBANK);
                        break;
                    case "I&M BANK":
                        payFromPesalink.setBank(IMBANK);
                        break;
                    case "KCB BANK":
                        payFromPesalink.setBank(KCBBANK);
                        break;

                }
                String t2 = getInstructionText();
                fragmentPayFromPesalinkPaymentBinding.mnoContentText.setText(t2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return fragmentPayFromPesalinkPaymentBinding.getRoot();
    }





    private String getInstructionText() {
        String t2 = getString(R.string.pesalink_payment_instructions);
        t2 = String.format(t2,payFromPesalink.getBank(),payFromPesalink.getUssdCode(),paymentVm.getMobPay().getMerchantConfig().getPesalinkAccount(),paymentVm.getPayment().getCurrency() +" " +paymentVm.getPayment().getAmount(),payFromPesalinkVM.getExternalTrasactionReference());
        return t2;
    }
}
