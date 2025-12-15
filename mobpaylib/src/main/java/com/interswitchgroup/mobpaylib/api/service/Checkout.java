package com.interswitchgroup.mobpaylib.api.service;

import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Checkout {
  @FormUrlEncoded
  @POST("/ipg-backend/api/checkout")
  Call<ResponseBody> transactionCheckout(@FieldMap Map<String, String> checkoutTransactionPayload);
}
