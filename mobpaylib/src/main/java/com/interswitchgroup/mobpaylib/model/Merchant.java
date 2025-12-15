package com.interswitchgroup.mobpaylib.model;

import java.io.Serializable;

public class Merchant implements Serializable {
  private String merchantId;
  private String domain;

  public Merchant() {}

  public Merchant(String merchantId, String domain) {
    this.merchantId = merchantId;
    this.domain = domain;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }
}
