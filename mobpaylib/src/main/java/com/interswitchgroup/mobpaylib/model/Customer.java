package com.interswitchgroup.mobpaylib.model;

public class Customer {
    private String someNumber1 = "";
    private String firstName = "";
    private String secondName = "";
    private String email = "";
    private String mobile = "";
    private String city = "";
    private String country = "";
    private String someNumber2 = "";
    private String area = "";

    public Customer() {
    }

    public Customer(String someNumber1, String firstName, String secondName, String email, String mobile, String city, String country, String someNumber2, String area) {
        this.someNumber1 = someNumber1;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.mobile = mobile;
        this.city = city;
        this.country = country;
        this.someNumber2 = someNumber2;
        this.area = area;
    }

    @Override
    public String toString() {
        return this.someNumber1 + "|" + this.firstName + "|" + this.secondName + "|" + this.email + "|" + this.mobile + "|" + this.city + "|" + this.country + "|" + this.someNumber2 + "|" + this.area + "|" + this.city;
    }
}
