package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CardPayment {
    @POST("/api/v1/merchant/transact/cards")
    Call<CardPaymentResponse> merchantCardPayment(@Body CardPaymentPayload cardPaymentPayload);
}
