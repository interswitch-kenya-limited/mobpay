package com.interswitchgroup.mobpaylib.model;

import java.io.Serializable;

public class Customer  implements Serializable {
    private String customerId = "";
    private String firstName = "";
    private String secondName = "";
    private String email = "";
    private String mobile = "";
    private String city = "";
    private String country = "";
    private String postalCode = "";
    private String street = "";
    private String state = "";

    public Customer() {
    }

    public Customer(String customerId) {
        this.customerId = customerId;
    }

    public Customer(String customerId, String firstName, String secondName, String email, String mobile, String city, String country, String postalCode, String street, String state) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.mobile = mobile;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.street = street;
        this.state = state;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.customerId + "|" + this.firstName + "|" + this.secondName + "|" + this.email + "|" + this.mobile + "|" + this.city + "|" + this.country + "|" + this.postalCode + "|" + this.street + "|" + this.city;
    }
}
