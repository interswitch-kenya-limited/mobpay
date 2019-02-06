package com.interswitchgroup.mobpaylib.model;

public class Transaction {
    private String id;
    private String reference;

    public Transaction(String transactionRef) {
        this.reference = transactionRef;
    }

    String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }
}
