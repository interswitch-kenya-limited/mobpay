package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

public class CardToken extends BaseObservable implements Serializable {
    private String token;
    private String expiry;
    private String panLast4Digits;
    private String cvv;

    public CardToken(String token, String cvv, String expiry) {
        this.token = token;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getPanLast4Digits() {
        return panLast4Digits;
    }

    public void setPanLast4Digits(String panLast4Digits) {
        this.panLast4Digits = panLast4Digits;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
