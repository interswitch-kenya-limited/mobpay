package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.interswitchgroup.mobpaylib.BR;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Bank extends BaseObservable implements Serializable {
    private Type type;
    private String provider;
    private boolean mobileFullyValid;
    private Pattern pattern;

    public Bank(Type type) {
        this.setType(type);
    }

    public Bank() {

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        switch (type) {
            case EQUITY:
                this.provider = "702";
                this.setPattern(Pattern.compile("^(\\+?254|0)[7]([0-2][0-9]|[9][0-3])[0-9]{0,6}$"));
                break;
            case STANCHART:
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


    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public enum Type {
        EQUITY("Equity"), STANCHART("Stanchart"), KCB("KCB"),STANBIC("Stanbic Bank");
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
