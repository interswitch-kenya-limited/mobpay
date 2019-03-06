package com.interswitchgroup.mobpaylib.ui.fragments.mobile;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;

import javax.inject.Inject;

public class MobileVm extends ViewModel {
    private static String LOG_TAG = MobileVm.class.getSimpleName();
    public static String EXPRESS = "Express";
    public static String PAYBIll = "Paybill";
    private String paymentMethod = EXPRESS;
    private PaymentVm paymentVm;
    private Mobile mobile;
    private TransactionFailureCallback expressTransactionFailureCallback;
    private TransactionFailureCallback paybillTransactionFailureCallback;

    @Inject
    public MobileVm() {
        this.mobile = new Mobile();
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentVm getPaymentVm() {
        return paymentVm;
    }

    public void setPaymentVm(PaymentVm paymentVm) {
        this.paymentVm = paymentVm;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public void setExpressTransactionFailureCallback(TransactionFailureCallback expressTransactionFailureCallback) {
        this.expressTransactionFailureCallback = expressTransactionFailureCallback;
    }

    public void setPaybillTransactionFailureCallback(TransactionFailureCallback paybillTransactionFailureCallback) {
        this.paybillTransactionFailureCallback = paybillTransactionFailureCallback;
    }

    public void makePayment() {
        if (paymentMethod.equalsIgnoreCase(EXPRESS)) {
            paymentVm.makeMobilePayment(mobile, expressTransactionFailureCallback);
        } else if (paymentMethod.equalsIgnoreCase(PAYBIll)) {
            Log.e(LOG_TAG, "Not working yet");
            paymentVm.confirmMobilePayment(paymentVm.getPayment().getOrderId(), paybillTransactionFailureCallback);
        }
    }
}
