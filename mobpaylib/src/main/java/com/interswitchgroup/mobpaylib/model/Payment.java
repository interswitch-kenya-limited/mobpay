package com.interswitchgroup.mobpaylib.model;

public class Payment {
    private String amount;
    private String transactionRef;
    private String terminalType;
    private String terminalId;
    private String paymentItem;
    private String currency;

    public Payment(String amount, String transactionRef, String terminalType, String terminalId, String paymentItem, String currency) {
        this.amount = amount;
        this.transactionRef = transactionRef;
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
}
