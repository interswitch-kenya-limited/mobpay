package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.ThreeDSecureAPIResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ThreeDSecure {
    @GET("https://testmerchant.interswitch-ke.com/merchant/card/initialize")
    Single<ThreeDSecureAPIResponse> cardInitialize(@Query("requestStr") CardPaymentPayload cardPaymentPayload);

    @GET("https://testmerchant.interswitch-ke.com/merchant/card/enrolled1")
    Single<ThreeDSecureAPIResponse> checkEnrollAction(@Query("requestStr") ThreeDSecureAPIResponse cardInitializeResponse, @Query("referenceId") String referenceId);
}
