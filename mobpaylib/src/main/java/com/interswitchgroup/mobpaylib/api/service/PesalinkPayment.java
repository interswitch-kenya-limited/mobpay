package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PesalinkPayment {

    @POST("merchant/transact/pesalink")
    Single<PesalinkPaymentResponse> pesalinkPayment(@Body PesalinkPaymentPayload pesalinkPaymentPayload);

}
