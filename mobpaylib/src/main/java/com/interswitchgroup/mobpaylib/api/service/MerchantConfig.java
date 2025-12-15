package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.MerchantConfigResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MerchantConfig {
  @GET("merchant/mfb/confignew")
  Call<MerchantConfigResponse> fetchMerchantConfig();
}
