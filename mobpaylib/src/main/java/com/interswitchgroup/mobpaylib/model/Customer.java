package com.interswitchgroup.mobpaylib.model;

import org.apache.commons.lang3.*;
import org.apache.commons.validator.routines.EmailValidator;

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

    public void validate() throws IllegalArgumentException {
        StringBuilder failedFields = new StringBuilder();

        if (StringUtils.isBlank(this.customerId)) {
            failedFields.append("customerId, ");
        }
        if (StringUtils.isBlank(this.firstName)) {
            failedFields.append("firstName, ");
        }
        if (StringUtils.isBlank(this.secondName)) {
            failedFields.append("secondName, ");
        }
        if (StringUtils.isBlank(this.email)) {
            failedFields.append("email, ");
        }
        if (StringUtils.isBlank(this.mobile)) {
            failedFields.append("mobile, ");
        }
        if (StringUtils.isBlank(this.city)) {
            failedFields.append("city, ");
        }
        if (StringUtils.isBlank(this.country)) {
            failedFields.append("country, ");
        }
        if (StringUtils.isBlank(this.postalCode)) {
            failedFields.append("postalCode, ");
        }
        if (StringUtils.isBlank(this.street)) {
            failedFields.append("street, ");
        }
        if (StringUtils.isBlank(this.state)) {
            failedFields.append("state, ");
        }

        if (failedFields.length() > 0) {
            // Remove the last comma and space
            failedFields.setLength(failedFields.length() - 2);
            throw new IllegalArgumentException("Validation failed for fields: " + failedFields);
        }
    }
}
