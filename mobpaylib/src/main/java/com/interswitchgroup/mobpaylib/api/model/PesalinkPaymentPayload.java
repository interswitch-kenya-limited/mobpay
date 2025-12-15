package com.interswitchgroup.mobpaylib.api.model;

import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

// TODO provider has been hardcoded
public class PesalinkPaymentPayload {
  private String amount;
  private String orderId;
  private String transactionRef;
  private String terminalType;
  private String paymentItem;
  private String provider;
  private String merchantId;
  private String isLog;
  private String currency;
  private String country;
  private String city;
  private String narration;

  public PesalinkPaymentPayload(Merchant merchant, Payment payment, Customer customer) {
    this.amount = payment.getAmount();
    this.orderId = payment.getOrderId();
    this.transactionRef = payment.getTransactionRef();
    this.terminalType = payment.getTerminalType();
    this.paymentItem = payment.getPaymentItem();
    this.provider = "941";
    this.merchantId = merchant.getMerchantId();
    this.isLog = "0";
    this.currency = payment.getCurrency();
    this.country = customer.getCountry();
    this.city = customer.getCity();
    this.narration = payment.getNarration();
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getTransactionRef() {
    return transactionRef;
  }

  public void setTransactionRef(String transactionRef) {
    this.transactionRef = transactionRef;
  }

  public String getTerminalType() {
    return terminalType;
  }

  public void setTerminalType(String terminalType) {
    this.terminalType = terminalType;
  }

  public String getPaymentItem() {
    return paymentItem;
  }

  public void setPaymentItem(String paymentItem) {
    this.paymentItem = paymentItem;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public String getIsLog() {
    return isLog;
  }

  public void setIsLog(String isLog) {
    this.isLog = isLog;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getNarration() {
    return narration;
  }

  public void setNarration(String narration) {
    this.narration = narration;
  }
}
