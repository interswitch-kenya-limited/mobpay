package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.CardPaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CardPayment {
    //    TODO set the correct urls and return types
    @POST("/charge/mobile_charge")
    Call<CardPaymentResponse> charge(@Body CardPaymentPayload cardPaymentPayload);

    @FormUrlEncoded
    @POST("/charge/validate")
    Call<CardPaymentResponse> validateCharge(@FieldMap HashMap<String, String> fields);
}
