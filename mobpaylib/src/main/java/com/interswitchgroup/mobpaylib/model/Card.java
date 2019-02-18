package com.interswitchgroup.mobpaylib.model;

import com.interswitchgroup.mobpaylib.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Card {
    private String pan;
    private String cvv;
    private String expiryYear;
    private String expiryMonth;
    public static final Map<Type, Integer> TYPE_DRAWABLE_MAP;
    public static final Map<Pattern, Type> PATTERN_TYPE_MAP;

    static {
        Map<Pattern, Type> aMap = new HashMap<>();
        aMap.put(Pattern.compile("^(((50)[0-9])|(650))[0-9]{0,16}$"), Type.VERVE);
        aMap.put(Pattern.compile("^4[0-9]{1,12}(?:[0-9]{6})?$"), Type.VISA);
        aMap.put(Pattern.compile("^5[1-5][0-9]{0,14}$"), Type.MASTERCARD);
        aMap.put(Pattern.compile("^6(?:011|5[0-9]{1,2})[0-9]{0,12}$"), Type.DISCOVER);
        aMap.put(Pattern.compile("^3[47][0-9]{0,13}$"), Type.AMEX);
        PATTERN_TYPE_MAP = Collections.unmodifiableMap(aMap);
    }

    static {
        Map<Type, Integer> aMap = new HashMap<>();
        aMap.put(Type.VISA, R.drawable.visa);
        aMap.put(Type.MASTERCARD, R.drawable.mastercard);
        aMap.put(Type.AMEX, R.drawable.amex);
        aMap.put(Type.DISCOVER, R.drawable.discover);
        aMap.put(Type.VERVE, R.drawable.verve);
        TYPE_DRAWABLE_MAP = Collections.unmodifiableMap(aMap);
    }

    public Card(String pan, String cvv, String expiryYear, String expiryMonth) {
        this.pan = pan;
        this.cvv = cvv;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public static Type getType(String cardNumber) {
        for (Pattern pattern : PATTERN_TYPE_MAP.keySet()) {
            Matcher m = pattern.matcher(cardNumber);
            if (m.find()) {
                return PATTERN_TYPE_MAP.get(pattern);
            }
        }
        return null;
    }

    public enum Type {
        VISA(1), MASTERCARD(2), AMEX(4), DISCOVER(8), VERVE(10);
        public int value;

        Type(int value) {
            this.value = value;
        }
    }
}
