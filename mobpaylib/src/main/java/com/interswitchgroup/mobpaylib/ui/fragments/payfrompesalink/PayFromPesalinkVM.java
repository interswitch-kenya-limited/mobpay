package com.interswitchgroup.mobpaylib.ui.fragments.payfrompesalink;

import androidx.lifecycle.ViewModel;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.model.PayFromPesalink;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;

public class PayFromPesalinkVM extends ViewModel {
  private static String LOG_TAG = PayFromPesalinkVM.class.getSimpleName();
  private PaymentVm paymentVm;
  private TransactionFailureCallback transactionFailureCallback;
  private PayFromPesalink payFromPesalink;
  private String externalTrasactionReference;

  public PayFromPesalinkVM() {

    this.payFromPesalink = new PayFromPesalink(PayFromPesalink.Bank.NCBA);
  }

  public PaymentVm getPaymentVm() {
    return paymentVm;
  }

  public void setPaymentVm(PaymentVm paymentVm) {
    this.paymentVm = paymentVm;
  }

  public TransactionFailureCallback getTransactionFailureCallback() {
    return transactionFailureCallback;
  }

  public void setTransactionFailureCallback(TransactionFailureCallback transactionFailureCallback) {
    this.transactionFailureCallback = transactionFailureCallback;
  }

  public String getExternalTrasactionReference() {
    return externalTrasactionReference;
  }

  public void setExternalTrasactionReference(String externalTrasactionReference) {
    this.externalTrasactionReference = externalTrasactionReference;
  }

  public PayFromPesalink getPayFromPesalink() {
    return payFromPesalink;
  }

  public void setPayFromPesalink(PayFromPesalink payFromPesalink) {
    this.payFromPesalink = payFromPesalink;
  }

  public void makePayment() {
    paymentVm.confrimTransactionPayment(
        paymentVm.getPayment().getTransactionRef(), transactionFailureCallback);
  }
}
