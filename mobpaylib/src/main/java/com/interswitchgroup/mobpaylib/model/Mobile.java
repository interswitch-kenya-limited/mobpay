package com.interswitchgroup.mobpaylib.model;

public class Mobile {
    private String phone;
    private Type type;
    private String provider;

    public Mobile(String phone, Type type) {
        this.phone = phone;
        this.setType(type);
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
