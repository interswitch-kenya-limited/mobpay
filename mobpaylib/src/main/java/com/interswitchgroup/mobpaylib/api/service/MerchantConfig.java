package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.MerchantConfigResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MerchantConfig {
    @GET("merchant/mfb/confignew")
    Single<MerchantConfigResponse> fetchMerchantConfig();
}
