package com.interswitchgroup.mobpaylib.model;

import com.interswitchgroup.mobpaylib.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Card implements Serializable {
    private String pan;
    private String cvv;
    private String expiryYear;
    private String expiryMonth;
    private String fullExpiry;
    public static final Map<Type, Integer> TYPE_DRAWABLE_MAP;
    public static final Map<Pattern, Type> PATTERN_TYPE_MAP;

    static {
        Map<Pattern, Type> aMap = new HashMap<>();
        // TODO Verve as is overlaps discover cards and does not handle verve international(6500---)
        aMap.put(Pattern.compile("^(50)[0-9]{0,17}$"), Type.VERVE);
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

    public Card() {

    }

    // Returns a sanitized string without any non-numeric characters
    public String getPan() {
        return pan != null ? pan.replaceAll("[^\\d.]", "") : pan;
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

    public String getFullExpiry() {
        return fullExpiry;
    }

    public void setFullExpiry(String fullExpiry) {
        if (fullExpiry != null) {
            fullExpiry = fullExpiry.replaceAll("[^\\d]", ""); // Remove non numeric characters including / separator
            String[] expiryParts = fullExpiry.split("(?<=\\G.{2})");// Split into twos
            if (expiryParts.length > 0) {
                this.expiryMonth = expiryParts[0];
                if (expiryParts.length > 1) {
                    this.expiryYear = expiryParts[1];
                }
            }
        }
        this.fullExpiry = fullExpiry;
    }

    public static Type getType(String cardNumber) {
        cardNumber = cardNumber.replaceAll("[^\\d.]", "");
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
