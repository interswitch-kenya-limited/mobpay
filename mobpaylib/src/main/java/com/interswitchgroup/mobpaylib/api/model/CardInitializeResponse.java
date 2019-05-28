package com.interswitchgroup.mobpaylib.api.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CardInitializeResponse {

    private String amount;
    private String merchantId;
    private String orderId;
    private String transactionRef;
    private String paymentItem;
    private String provider;
    private String customerId;
    private String transactionType;
    private String terminalType;
    private String narration;
    private String authData;
    private String toInstCode;
    private String toAccount;
    private String token;
    private String pin;
    private String terminalId;
    private String country;
    private String city;
    private String currency;
    private String customerInfor;
    private String recipientName;
    private String senderName;
    private String fromInstCode;
    private String fromAccount;
    private String fee;
    private String domain;
    private String preauth;
    private String csReferenceID;
    private String csTransactionMode;
    private String csAcsURL;
    private String csPaReq;
    private String csAuthenticationTransactionID;
    private String csUcafAuthenticationData;
    private String csUcafCollectionIndicator;
    private String csCavv;
    private String csCavvAlgorithm;
    private String csCommerceIndicator;
    private String csEciRaw;
    private String csXid;
    private String csVeresEnrolled;
    private String csParesStatus;
    private String paca;
    private String jwt;
    private String pan;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getToInstCode() {
        return toInstCode;
    }

    public void setToInstCode(String toInstCode) {
        this.toInstCode = toInstCode;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerInfor() {
        return customerInfor;
    }

    public void setCustomerInfor(String customerInfor) {
        this.customerInfor = customerInfor;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getFromInstCode() {
        return fromInstCode;
    }

    public void setFromInstCode(String fromInstCode) {
        this.fromInstCode = fromInstCode;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPreauth() {
        return preauth;
    }

    public void setPreauth(String preauth) {
        this.preauth = preauth;
    }

    public String getCsReferenceID() {
        return csReferenceID;
    }

    public void setCsReferenceID(String csReferenceID) {
        this.csReferenceID = csReferenceID;
    }

    public String getCsTransactionMode() {
        return csTransactionMode;
    }

    public void setCsTransactionMode(String csTransactionMode) {
        this.csTransactionMode = csTransactionMode;
    }

    public String getCsAcsURL() {
        return csAcsURL;
    }

    public void setCsAcsURL(String csAcsURL) {
        this.csAcsURL = csAcsURL;
    }

    public String getCsPaReq() {
        return csPaReq;
    }

    public void setCsPaReq(String csPaReq) {
        this.csPaReq = csPaReq;
    }

    public String getCsAuthenticationTransactionID() {
        return csAuthenticationTransactionID;
    }

    public void setCsAuthenticationTransactionID(String csAuthenticationTransactionID) {
        this.csAuthenticationTransactionID = csAuthenticationTransactionID;
    }

    public String getCsUcafAuthenticationData() {
        return csUcafAuthenticationData;
    }

    public void setCsUcafAuthenticationData(String csUcafAuthenticationData) {
        this.csUcafAuthenticationData = csUcafAuthenticationData;
    }

    public String getCsUcafCollectionIndicator() {
        return csUcafCollectionIndicator;
    }

    public void setCsUcafCollectionIndicator(String csUcafCollectionIndicator) {
        this.csUcafCollectionIndicator = csUcafCollectionIndicator;
    }

    public String getCsCavv() {
        return csCavv;
    }

    public void setCsCavv(String csCavv) {
        this.csCavv = csCavv;
    }

    public String getCsCavvAlgorithm() {
        return csCavvAlgorithm;
    }

    public void setCsCavvAlgorithm(String csCavvAlgorithm) {
        this.csCavvAlgorithm = csCavvAlgorithm;
    }

    public String getCsCommerceIndicator() {
        return csCommerceIndicator;
    }

    public void setCsCommerceIndicator(String csCommerceIndicator) {
        this.csCommerceIndicator = csCommerceIndicator;
    }

    public String getCsEciRaw() {
        return csEciRaw;
    }

    public void setCsEciRaw(String csEciRaw) {
        this.csEciRaw = csEciRaw;
    }

    public String getCsXid() {
        return csXid;
    }

    public void setCsXid(String csXid) {
        this.csXid = csXid;
    }

    public String getCsVeresEnrolled() {
        return csVeresEnrolled;
    }

    public void setCsVeresEnrolled(String csVeresEnrolled) {
        this.csVeresEnrolled = csVeresEnrolled;
    }

    public String getCsParesStatus() {
        return csParesStatus;
    }

    public void setCsParesStatus(String csParesStatus) {
        this.csParesStatus = csParesStatus;
    }

    public String getPaca() {
        return paca;
    }

    public void setPaca(String paca) {
        this.paca = paca;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        // Java object to JSON string
        String jsonString = super.toString();
        try {
            jsonString = mapper.writeValueAsString(this);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
