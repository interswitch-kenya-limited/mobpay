package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

public class MobileMoneyProvider extends BaseObservable implements Serializable {
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
