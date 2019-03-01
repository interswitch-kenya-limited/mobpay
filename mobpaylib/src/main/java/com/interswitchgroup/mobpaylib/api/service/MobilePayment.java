package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MobilePayment {
    @POST("merchant/transact/bills")
    Single<CardPaymentResponse> mobilePayment(@Body MobilePaymentPayload mobilePaymentPayload);
}
