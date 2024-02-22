package com.interswitchgroup.mobpaylib.utils;

public class InterswitchException extends Throwable {


    private String code;
    private String backendResponse;
    public InterswitchException(String message,String code,String backendResponse) {
        super(message);
        this.code = code;
        this.backendResponse = backendResponse;
    }


    public String getCode() {
        return code;
    }

    public String getBackendResponse() {
        return backendResponse;
    }
}
