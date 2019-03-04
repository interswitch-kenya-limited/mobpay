package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.interswitchgroup.mobpaylib.BR;

import java.io.Serializable;

public class Mobile extends BaseObservable implements Serializable {
    private String phone;
    private Type type;
    private String provider;
    public boolean valid;

    public Mobile(String phone, Type type) {
        this.phone = phone;
        this.setType(type);
    }

    public Mobile() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        switch (type) {
            case MPESA:
                this.provider = "702";
                break;
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
    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }

    public enum Type {
        MPESA("MPesa"), EAZZYPAY("Equitel Eazzy Pay");
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
