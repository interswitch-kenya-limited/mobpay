package com.interswitchgroup.mobpaylib.model;

public class Merchant {
    private String merchantId;
    private String authData;
    private String domain;

    public Merchant(String merchantId, String authData, String domain) {
        this.merchantId = merchantId;
        this.authData = authData;
        this.domain = domain;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
