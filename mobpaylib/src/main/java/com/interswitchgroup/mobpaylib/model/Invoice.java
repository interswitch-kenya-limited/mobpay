package com.interswitchgroup.mobpaylib.model;

public class Invoice {
    private Customer customer;

    public Invoice() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
