package com.interswitchgroup.mobpaylib.api.model;

public class PaybillQueryResponse implements TransactionResponse {
    private String transactionAmount;
    private String transactionRef;
    private String mpesaReceipt;
    private String orderId;
    private String transactionResponseCode;
    private String transactionResponseMessage;

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getMpesaReceipt() {
        return mpesaReceipt;
    }

    public void setMpesaReceipt(String mpesaReceipt) {
        this.mpesaReceipt = mpesaReceipt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionResponseCode() {
        return transactionResponseCode;
    }

    public void setTransactionResponseCode(String transactionResponseCode) {
        this.transactionResponseCode = transactionResponseCode;
    }

    public String getTransactionResponseMessage() {
        return transactionResponseMessage;
    }

    public void setTransactionResponseMessage(String transactionResponseMessage) {
        this.transactionResponseMessage = transactionResponseMessage;
    }

    @Override
    public String getTransactionOrderId() {
        return this.getOrderId();
    }
}
