package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MobilePayment {
    @POST("merchant/transact/bills")
    Single<MobilePaymentResponse> mobilePayment(@Body MobilePaymentPayload mobilePaymentPayload);
}
