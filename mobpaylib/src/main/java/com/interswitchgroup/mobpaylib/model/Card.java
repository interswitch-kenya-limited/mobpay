package com.interswitchgroup.mobpaylib.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.interswitchgroup.mobpaylib.BR;
import com.interswitchgroup.mobpaylib.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Card extends BaseObservable implements Serializable {
    private String pan;
    private String cvv;
    private String expiryYear;
    private String expiryMonth;
    private String fullExpiry;
    public static final Map<Type, Integer> TYPE_DRAWABLE_MAP;
    public static final Map<Pattern, Type> ACCEPTED_CARD_PATTERN_TYPES;
    public boolean valid;

    static {
        Map<Pattern, Type> aMap = new HashMap<>();
        // TODO Verve as is overlaps discover cards and does not handle verve international(6500---)
        aMap.put(Pattern.compile("^(50)[0-9]{0,17}$"), Type.VERVE);
        aMap.put(Pattern.compile("^4[0-9]{1,12}(?:[0-9]{6})?$"), Type.VISA);
        aMap.put(Pattern.compile("^5[1-5][0-9]{0,14}$"), Type.MASTERCARD);
//        aMap.put(Pattern.compile("^6(?:011|5[0-9]{1,2})[0-9]{0,12}$"), Type.DISCOVER);
//        aMap.put(Pattern.compile("^3[47][0-9]{0,13}$"), Type.AMEX);
        ACCEPTED_CARD_PATTERN_TYPES = Collections.unmodifiableMap(aMap);
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
        this.valid = isCardValid();
    }

    public Card() {
        this.valid = isCardValid();
    }

    // Returns a sanitized string without any non-numeric characters
    public String getPan() {
        return pan != null ? pan.replaceAll("[^\\d]", "") : pan;
    }

    public void setPan(String pan) {
        this.pan = pan != null ? pan.replaceAll("[^\\d]", "") : pan;
        setValid(isCardValid());
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
        setValid(isCardValid());
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
        setValid(isCardValid());
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
        setValid(isCardValid());
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
        setValid(isCardValid());
    }

    @Bindable
    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }

    public static Type getType(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return null;
        }
        cardNumber = cardNumber.replaceAll("[^\\d.]", "");
        for (Pattern pattern : ACCEPTED_CARD_PATTERN_TYPES.keySet()) {
            Matcher m = pattern.matcher(cardNumber);
            if (m.find()) {
                return ACCEPTED_CARD_PATTERN_TYPES.get(pattern);
            }
        }
        return null;
    }

    public static boolean isPanValid(String pan) {
        if (pan == null || pan.isEmpty()) {
            return false;
        }
        pan = pan.replaceAll("[^\\d]", "");
        Type type = getType(pan);
        if (type != null) {
            switch (type) {
                case VERVE:
                    if (pan.length() == 16 || pan.length() == 19) {
                        return true;
                    }
                default:
                    if (pan.length() == 16) {
                        return true;
                    }
            }
        }
        return false;
    }

    public static boolean isCvvValid(String cvv) {
        return cvv != null && cvv.length() == 3;
    }

    private static Date getExpiryAsDate(String fullExpiry) {
        if (fullExpiry == null || fullExpiry.isEmpty()) {
            return null;
        }
        String[] expiryParts = fullExpiry.replaceAll("[^\\d]", "").split("(?<=\\G.{2})");
        if (expiryParts.length < 2) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, Integer.valueOf("20" + expiryParts[1]));
        calendar.set(Calendar.MONTH, Integer.valueOf(expiryParts[0]));
        return calendar.getTime();
    }

    public static boolean isExpiryValid(String fullExpiry) {
        Date expDate = getExpiryAsDate(fullExpiry);
        return expDate != null && expDate.getTime() > new Date().getTime();
    }

    private boolean isCardValid() {
        boolean cvvValid = isCvvValid(this.cvv);
        boolean panValid = isPanValid(this.pan);
        boolean expiryValid = isExpiryValid(this.fullExpiry);
        return cvvValid && panValid && expiryValid;
    }


    public enum Type {
        VISA(1), MASTERCARD(2), AMEX(4), DISCOVER(8), VERVE(10);
        public int value;

        Type(int value) {
            this.value = value;
        }
    }
}
