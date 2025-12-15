package com.interswitchgroup.mobpaylib.ui.fragments.card;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;
import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.PesalinkSuccessCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.model.Payment;

public class PaymentVm extends ViewModel {
  private String LOG_TAG = this.getClass().getSimpleName();
  private MobPay mobPay;
  private ObservableBoolean loading = new ObservableBoolean(false);
  private Merchant merchant;
  private Payment payment;
  private Customer customer;
  private TransactionSuccessCallback onSuccess;
  private TransactionFailureCallback onFailure;

  public PaymentVm() {}

  public MobPay getMobPay() {
    return mobPay;
  }

  public void setMobPay(MobPay mobPay) {
    this.mobPay = mobPay;
  }

  public ObservableBoolean getLoading() {
    return loading;
  }

  public void setLoading(ObservableBoolean loading) {
    this.loading = loading;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public void setOnSuccess(TransactionSuccessCallback onSuccess) {
    this.onSuccess = onSuccess;
  }

  public void setOnFailure(TransactionFailureCallback onFailure) {
    this.onFailure = onFailure;
  }

  public void makeCardPayment(Card card) {
    loading.set(true);
    mobPay.makeCardPayment(card, merchant, payment, customer, onSuccess, onFailure);
  }

  public void makeCardTokenPayment(CardToken cardToken) {
    loading.set(true);
    mobPay.makeCardTokenPayment(cardToken, merchant, payment, customer, onSuccess, onFailure);
  }

  public void makeMobilePayment(
      Mobile mobile, TransactionFailureCallback expressTransactionFailureCallback) {
    loading.set(true);
    mobPay.makeMobileMoneyPayment(
        mobile, merchant, payment, customer, onSuccess, expressTransactionFailureCallback);
  }

  public void confirmMobilePayment(
      String orderId, TransactionFailureCallback paybillTransactionFailureCallback) {
    loading.set(true);
    mobPay.confirmMobilePayment(orderId, onSuccess, paybillTransactionFailureCallback);
  }

  public void confrimTransactionPayment(
      String transactionRef, TransactionFailureCallback payFromPesalinkTransactionFailureCallback) {
    loading.set(true);
    mobPay.confirmTransactionPayment(
        transactionRef, onSuccess, payFromPesalinkTransactionFailureCallback);
  }

  public void getExternalTransactionReference(
      String transactionRef,
      final PesalinkSuccessCallback pesalinkSuccessCallback,
      PesalinkFailureCallback pesalinkFailureCallback) {
    mobPay.makePesalinkPayment(
        merchant, payment, customer, pesalinkSuccessCallback, pesalinkFailureCallback);
  }
}
