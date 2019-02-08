package com.interswitchgroup.mobpaylib.api.model;

import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

public class CardPaymentPayload extends TransactionPayload {

    private String provider;
    private String authData;
    private String narration;

    public CardPaymentPayload(Merchant merchant, Payment payment, Customer customer, String authData) {
        super(merchant, payment, customer);
        this.authData = authData;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}