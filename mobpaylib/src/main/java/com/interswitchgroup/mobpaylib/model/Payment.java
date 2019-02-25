package com.interswitchgroup.mobpaylib.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Payment implements Serializable {
    private String amount;
    private String transactionRef;
    private String orderId;
    private String terminalType;
    private String terminalId;
    private String paymentItem;
    private String currency;
    private String preauth;

    public Payment(String amount, String transactionRef, String terminalType, String terminalId, String paymentItem, String currency, String orderId) {
        this.amount = amount;
        this.transactionRef = transactionRef;
        this.orderId = orderId;
        this.terminalType = terminalType;
        this.terminalId = terminalId;
        this.paymentItem = paymentItem;
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmountString() {
//        return String.format("%,.2f", Double.valueOf(amount) / 100); // Constant of two decimal places with up to two trailing zeros
        return new DecimalFormat("#,###.##").format(Double.valueOf(amount) / 100); // Up to two decimal places but no trailing zeros
    }

    public String getPreauth() {
        return preauth;
    }

    public void setPreauth(String preauth) {
        this.preauth = preauth;
    }
}
