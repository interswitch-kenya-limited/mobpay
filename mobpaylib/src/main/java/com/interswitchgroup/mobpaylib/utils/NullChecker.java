package com.interswitchgroup.mobpaylib.utils;

public class NullChecker {
    public static <T> T checkNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
