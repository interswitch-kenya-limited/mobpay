package com.interswitchgroup.mobpaylib.interfaces;


public interface TransactionFailureCallback {
    void onError(Throwable error);
}
