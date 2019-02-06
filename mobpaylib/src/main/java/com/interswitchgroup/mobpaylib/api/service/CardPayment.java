package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CardPayment {
    @POST("/api/v1/merchant/transact/cards")
    Single<CardPaymentResponse> merchantCardPayment(@Body CardPaymentPayload cardPaymentPayload);
}
