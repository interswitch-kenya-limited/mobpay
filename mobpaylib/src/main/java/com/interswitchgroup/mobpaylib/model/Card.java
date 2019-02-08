package com.interswitchgroup.mobpaylib.model;

public class Card {
    private String pan;
    private String cvv;
    private String expiryYear;
    private String expiryMonth;

    public Card(String pan, String cvv, String expiryYear, String expiryMonth) {
        this.pan = pan;
        this.cvv = cvv;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }
}
