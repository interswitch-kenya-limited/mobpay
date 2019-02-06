package com.interswitchgroup.mobpaylib.api.model;

public class CardPaymentPayload {
    private String amount;
    private String orderId;
    private String transactionRef;
    private String terminalType;
    private String terminalId;
    private String paymentItem;
    private String provider;
    private String merchantId;
    private String authData;
    private String customerInfor;
    private String currency;
    private String country;
    private String city;
    private String narration;
    private String domain;

    public CardPaymentPayload(String amount, String transactionRef, String terminalType, String terminalId, String paymentItem, String merchantId, String authData, String currency, String domain) {
        this.amount = amount;
        this.transactionRef = transactionRef;
        this.terminalType = terminalType;
        this.terminalId = terminalId;
        this.paymentItem = paymentItem;
        this.merchantId = merchantId;
        this.authData = authData;
        this.currency = currency;
        this.domain = domain;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getCustomerInfor() {
        return customerInfor;
    }

    public void setCustomerInfor(String customerInfor) {
        this.customerInfor = customerInfor;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}