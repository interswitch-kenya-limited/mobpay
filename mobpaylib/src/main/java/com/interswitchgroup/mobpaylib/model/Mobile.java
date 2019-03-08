package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.interswitchgroup.mobpaylib.BR;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Mobile extends BaseObservable implements Serializable {
    private String phone;
    private Type type;
    private String provider;
    private boolean mobileFullyValid;
    private boolean mobilePartiallyValid;
    private Pattern pattern;

    public Mobile(String phone, Type type) {
        this.phone = phone;
        this.setType(type);
    }

    public Mobile() {

    }

    public void refreshValidity() {
        setMobilePartiallyValid(this.pattern != null && this.phone != null && this.pattern.matcher(this.phone).matches());
        setMobileFullyValid(this.mobilePartiallyValid && phone.length() == 10);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        refreshValidity();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        switch (type) {
            case MPESA:
                this.provider = "702";
                this.setPattern(Pattern.compile("^(\\+?254|0)[7]([0-2][0-9]|[9][0-3])[0-9]{0,6}$"));
                break;
            case EAZZYPAY:
                this.provider = "708";
                this.setPattern(Pattern.compile("^(?:254|\\+254|0)?76[34]([0-9]{0,6})$"));
                break;
            default:
                throw new IllegalArgumentException("The type selected does not have a corresponding provider set");
        }
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Bindable
    public boolean isMobileFullyValid() {
        return mobileFullyValid;
    }

    public void setMobileFullyValid(boolean mobileFullyValid) {
        this.mobileFullyValid = mobileFullyValid;
        notifyPropertyChanged(BR.mobileFullyValid);
    }

    @Bindable
    public boolean isMobilePartiallyValid() {
        return mobilePartiallyValid;
    }

    public void setMobilePartiallyValid(boolean mobilePartiallyValid) {
        this.mobilePartiallyValid = mobilePartiallyValid;
        notifyPropertyChanged(BR.mobilePartiallyValid);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        refreshValidity();
    }

    public enum Type {
        MPESA("M-PESA"), EAZZYPAY("Eazzy Pay");
        public String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
