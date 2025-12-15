package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.MobilePaymentPayload;
import com.interswitchgroup.mobpaylib.api.model.MobilePaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.PaybillQueryResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MobilePayment {
  @POST("merchant/transact/bills")
  Single<MobilePaymentResponse> mobilePayment(@Body MobilePaymentPayload mobilePaymentPayload);

  @GET("merchant/bills/transactions/{transactionRef}")
  Single<PaybillQueryResponse> confirmMobilePayment(@Path("transactionRef") String transactionRef);
}
