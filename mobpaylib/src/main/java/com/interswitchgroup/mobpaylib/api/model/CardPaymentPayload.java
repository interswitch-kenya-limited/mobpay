package com.interswitchgroup.mobpaylib.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

public class CardPaymentPayload extends TransactionPayload {

    private String provider;
    private String authData;
    private String paca = "1";

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

    public String getPaca() {
        return paca;
    }

    public void setPaca(String paca) {
        this.paca = paca;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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