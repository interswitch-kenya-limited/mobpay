package com.interswitchgroup.mobpaylib.api.model;

import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

class TransactionPayload {
    private final String amount;
    private final String transactionRef;
    private final String preauth;
    private final String orderId;
    private final String terminalType;
    private final String terminalId;
    private final String paymentItem;
    private final String merchantId;
    private final String customerInfor;
    private final String currency;
    private final String country;
    private final String city;
    private final String domain;
    private final String narration;

    public TransactionPayload(Merchant merchant, Payment payment, Customer customer) {
        this.amount = payment.getAmount();
        this.transactionRef = payment.getTransactionRef();
        this.orderId = payment.getOrderId();
        this.terminalType = payment.getTerminalType();
        this.terminalId = payment.getTerminalId();
        this.paymentItem = payment.getPaymentItem();
        this.merchantId = merchant.getMerchantId();
        this.currency = payment.getCurrency();
        this.domain = merchant.getDomain();
        this.customerInfor = customer.toString();
        this.country = customer.getCountry();
        this.city = customer.getCity();
        this.preauth = payment.getPreauth();
        this.narration = payment.getNarration();
    }

    public String getAmount() {
        return amount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public String getPaymentItem() {
        return paymentItem;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCustomerInfor() {
        return customerInfor;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDomain() {
        return domain;
    }

    public String getPreauth() {
        return preauth;
    }

    public String getNarration() {
        return narration;
    }
}
