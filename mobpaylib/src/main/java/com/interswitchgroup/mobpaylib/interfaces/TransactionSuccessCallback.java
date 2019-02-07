package com.interswitchgroup.mobpaylib.interfaces;

import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;

public interface TransactionSuccessCallback<T extends TransactionResponse> {
    void onSuccess(T transactionResponse);
}
