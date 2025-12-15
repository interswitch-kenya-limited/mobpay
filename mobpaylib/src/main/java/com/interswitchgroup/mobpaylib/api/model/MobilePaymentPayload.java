package com.interswitchgroup.mobpaylib.api.model;

import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Mobile;
import com.interswitchgroup.mobpaylib.model.Payment;

public class MobilePaymentPayload extends TransactionPayload {

  private String provider;
  private String phone;

  public MobilePaymentPayload(
      Merchant merchant, Payment payment, Customer customer, Mobile mobile) {
    super(merchant, payment, customer);
    this.provider = mobile.getProvider();
    this.phone = mobile.getPhone();
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }
}
