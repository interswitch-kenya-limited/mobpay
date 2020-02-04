package com.interswitchgroup.mobpaylib.api.model;

public class PesalinkPaymentResponse {
    private String responseCode;
    private String responseMessage;
    private String externalPaymentRef;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getExternalPaymentRef() {
        return externalPaymentRef;
    }

    public void setExternalPaymentRef(String externalPaymentRef) {
        this.externalPaymentRef = externalPaymentRef;
    }
}
