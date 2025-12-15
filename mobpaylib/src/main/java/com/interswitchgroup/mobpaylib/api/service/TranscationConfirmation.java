package com.interswitchgroup.mobpaylib.api.service;

import com.interswitchgroup.mobpaylib.api.model.PaybillQueryResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TranscationConfirmation {
  @GET("merchant/bills/transactions/{transactionRef}")
  Single<PaybillQueryResponse> confirmTransanction(@Path("transactionRef") String transactionRef);
}
