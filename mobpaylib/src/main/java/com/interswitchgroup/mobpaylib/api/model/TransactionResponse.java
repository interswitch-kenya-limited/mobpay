package com.interswitchgroup.mobpaylib.api.model;

public interface TransactionResponse {
  String getTransactionOrderId();

  String getResponseCode();

  String getResponseMessage();
}
