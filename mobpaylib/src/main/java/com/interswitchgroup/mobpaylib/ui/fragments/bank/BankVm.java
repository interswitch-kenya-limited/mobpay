package com.interswitchgroup.mobpaylib.ui.fragments.bank;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.Bank;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;


public class BankVm extends ViewModel {
    public static String EXPRESS = "Express";
    private static String LOG_TAG = BankVm.class.getSimpleName();
    private PaymentVm paymentVm;
    private Bank bank;
    private TransactionFailureCallback expressTransactionFailureCallback;
    private TransactionFailureCallback paybillTransactionFailureCallback;

    public BankVm() {
        this.bank = new Bank();
    }


    public PaymentVm getPaymentVm() {
        return paymentVm;
    }

    public void setPaymentVm(PaymentVm paymentVm) {
        this.paymentVm = paymentVm;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setExpressTransactionFailureCallback(TransactionFailureCallback expressTransactionFailureCallback) {
        this.expressTransactionFailureCallback = expressTransactionFailureCallback;
    }

    public void setPaybillTransactionFailureCallback(TransactionFailureCallback paybillTransactionFailureCallback) {
        this.paybillTransactionFailureCallback = paybillTransactionFailureCallback;
    }

    public void makePayment(Activity activity) {
        try {
            MobPay.getInstance(activity,"IKIA264751EFD43881E84150FDC4D7F0717AD27C4E64", "J3e432fg5qdpFXDsjlinBPGs/CgCNaUs5BHLFloO3/U=", null).doSomeHover(activity,"0793720223","5","c09cf6ba");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}