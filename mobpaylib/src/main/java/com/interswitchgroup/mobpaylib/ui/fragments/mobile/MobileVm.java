package com.interswitchgroup.mobpaylib.ui.fragments.mobile;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;

public class MobileVm extends ViewModel {
  private static String LOG_TAG = MobileVm.class.getSimpleName();
  public static String EXPRESS = "Express";
  public static String PAYBIll = "Paybill";
  public String providerHint = "0700 000 000";
  private String paymentMethod = EXPRESS;
  private PaymentVm paymentVm;
  private Mobile mobile;
  private TransactionFailureCallback expressTransactionFailureCallback;
  private TransactionFailureCallback paybillTransactionFailureCallback;

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

  public void setExpressTransactionFailureCallback(
      TransactionFailureCallback expressTransactionFailureCallback) {
    this.expressTransactionFailureCallback = expressTransactionFailureCallback;
  }

  public void setPaybillTransactionFailureCallback(
      TransactionFailureCallback paybillTransactionFailureCallback) {
    this.paybillTransactionFailureCallback = paybillTransactionFailureCallback;
  }

  public void makePayment() {
    if (paymentMethod.equalsIgnoreCase(EXPRESS)) {
      paymentVm.makeMobilePayment(mobile, expressTransactionFailureCallback);
    } else if (paymentMethod.equalsIgnoreCase(PAYBIll)) {
      Log.e(LOG_TAG, "Not yet working");
      paymentVm.confirmMobilePayment(
          paymentVm.getPayment().getOrderId(), paybillTransactionFailureCallback);
    }
  }
}
