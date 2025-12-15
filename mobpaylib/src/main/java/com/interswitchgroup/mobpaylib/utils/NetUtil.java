package com.interswitchgroup.mobpaylib.utils;

import com.google.gson.Gson;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import retrofit2.HttpException;
import retrofit2.Response;

public class NetUtil {
  public static ErrorWrapper parseError(Throwable error) {
    ErrorWrapper errorWrapper = null;
    if (error instanceof HttpException) {
      try {
        Response response = ((HttpException) error).response();
        if (response.errorBody() != null
            && response.errorBody().contentType().toString().equalsIgnoreCase("application/json")) {
          errorWrapper = new Gson().fromJson(response.errorBody().string(), ErrorWrapper.class);
        } else {
          errorWrapper = null;
        }
      } catch (Exception ignored) {
        ignored.printStackTrace();
      }
    }
    return errorWrapper;
  }
}
