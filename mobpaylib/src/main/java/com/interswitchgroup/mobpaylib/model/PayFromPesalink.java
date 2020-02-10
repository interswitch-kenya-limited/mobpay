package com.interswitchgroup.mobpaylib.model;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class PayFromPesalink extends BaseObservable implements Serializable {
    private Bank bank;
    private String ussdCode;

    public PayFromPesalink(Bank bank){
        this.bank = bank;
        this.setBank(bank);
    }

    public Bank getBank() {
        return bank;
    }

    public String getUssdCode() {
        return ussdCode;
    }

    public void setUssdCode(String ussdCode) {
        this.ussdCode = ussdCode;
    }

    public void setBank(Bank bank) {
        switch (bank) {
            case CBA:
                this.ussdCode = "702";
                break;
            case EQUITY:
                this.ussdCode = "708";
                break;
            default:
                throw new IllegalArgumentException("The type selected does not have a corresponding provider set");
        }
        this.bank = bank;
    }



    public enum Bank{
        CBA("CBA"), EQUITY("EQUITY");
        public String value;

        Bank(String value){
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }

    }
}
