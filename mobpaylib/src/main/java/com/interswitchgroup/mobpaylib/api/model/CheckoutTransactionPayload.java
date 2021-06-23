package com.interswitchgroup.mobpaylib.api.model;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CheckoutTransactionPayload {
    public String merchantCode;
    public String domain;


    //payment details
    public String transactionReference;
    public String orderId;
    public String expiryTime;
    public String currencyCode;
    public String amount;
    public String narration;
    public String redirectUrl;
    public String iconUrl;
    public String merchantName;
    public String providerIconUrl;
    public String cardTokensJson;
    public String reqId;
    public String field1;
    public String dateOfPayment;
    public String terminalId = "3TLP0001";
    public String terminalType = "Android";
    public String channel = "MOBILE";
    public int fee = 0;
    public int preauth;


    //customer details
    public String customerId;
    public String customerFirstName;
    public String customerSecondName;
    public String customerEmail;
    public String customerCity;
    public String customerMobile;
    public String customerCountry;
    public String customerPostalCode;
    public String customerStreet;
    public String customerState;

    public CheckoutTransactionPayload(Merchant merchant, Payment payment, Customer customer, MobPay.Config config) {
        this.merchantCode = merchant.getMerchantId();
        this.domain = merchant.getDomain();
        this.transactionReference = payment.getTransactionRef();
        this.orderId = payment.getOrderId();
//        this.expiryTime = expiryTime;
        this.currencyCode = payment.getCurrency();
        this.amount = payment.getAmount();
        this.narration = payment.getNarration();
        this.redirectUrl = "https://uat.quickteller.co.ke/";
        this.iconUrl = config.getIconUrl();
//        this.merchantName = merchantName;
//        this.providerIconUrl = providerIconUrl;
//        this.cardTokensJson = cardTokensJson;
//        this.terminalId = terminalId;
//        this.terminalType = terminalType;
//        this.channel = channel;
//        this.fee = fee;
        this.preauth = 0;
        this.customerId = customer.getCustomerId();
        this.customerFirstName = customer.getFirstName();
        this.customerSecondName = customer.getSecondName();
        this.customerEmail = customer.getEmail();
        this.customerCity = customer.getCity();
        this.customerMobile = customer.getMobile();
        this.customerCountry = customer.getCountry();
        this.customerPostalCode = customer.getPostalCode();
        this.customerStreet = customer.getStreet();
        this.customerState = customer.getState();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
        this.dateOfPayment = sdf.format(new Date());
    }
}
