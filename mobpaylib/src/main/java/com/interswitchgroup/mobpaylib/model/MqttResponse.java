package com.interswitchgroup.mobpaylib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MqttResponse implements TransactionResponse {

  @JsonProperty("amount")
  public String amount;

  @JsonProperty("responseCode")
  public String responseCode;

  @JsonProperty("responseMessage")
  public String responseMessage;

  @JsonProperty("transactionAmount")
  public String transactionAmount;

  @JsonProperty("transactionRef")
  public String transactionRef;

  @JsonProperty("orderId")
  public String orderId;

  @JsonProperty("provider")
  public String provider;

  @JsonProperty("transactionState")
  public String transactionState;

  @JsonProperty("merchantId")
  public String merchantId;

  @JsonProperty("resultCode")
  public String resultCode;

  @JsonProperty("resultMessage")
  public String resultMessage;

  @JsonProperty("paymentItem")
  public String paymentItem;

  public MqttResponse() {}

  @Override
  public String getTransactionOrderId() {
    return orderId;
  }

  @Override
  public String getResponseCode() {
    return responseCode;
  }

  @Override
  public String getResponseMessage() {
    return responseMessage;
  }
}
