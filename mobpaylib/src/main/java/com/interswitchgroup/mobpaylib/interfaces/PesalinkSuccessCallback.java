package com.interswitchgroup.mobpaylib.interfaces;

import com.interswitchgroup.mobpaylib.api.model.PesalinkPaymentResponse;

public interface PesalinkSuccessCallback<P extends PesalinkPaymentResponse> {
  void onSuccess(P pesalinkResponse);
}
