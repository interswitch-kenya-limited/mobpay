package com.interswitchgroup.mobpaylib.model;

import androidx.databinding.BaseObservable;
import com.interswitchgroup.mobpaylib.R;
import java.io.Serializable;
import java.util.Objects;

public class CardToken extends BaseObservable implements Serializable {
  private String token;
  private String expiry;
  private String panLast4Digits;
  private String panFirst6Digits;
  private String cvv;

  public CardToken(String token, String expiry) {
    this.token = token;
    this.expiry = expiry;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getExpiry() {
    return expiry;
  }

  public void setExpiry(String expiry) {
    this.expiry = expiry;
  }

  public String getPanLast4Digits() {
    return panLast4Digits;
  }

  public void setPanLast4Digits(String panLast4Digits) {
    this.panLast4Digits = panLast4Digits;
  }

  public String getPanFirst6Digits() {
    return panFirst6Digits;
  }

  public void setPanFirst6Digits(String panFirst6Digits) {
    this.panFirst6Digits = panFirst6Digits;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  public Integer getImageResource() {
    Integer imageResource = Card.TYPE_DRAWABLE_MAP.get(Card.getType(panFirst6Digits));
    if (imageResource == null || imageResource <= 0) {
      imageResource = R.drawable.creditcard;
    }
    return imageResource;
  }

  public static String getDateForDisplay(String apiDate) {
    String expiryYear = "";
    String expiryMonth = "";
    if (apiDate != null) {
      apiDate =
          apiDate.replaceAll("[^\\d]", ""); // Remove non numeric characters including / separator
      String[] expiryParts = apiDate.split("(?<=\\G.{2})"); // Split into twos
      if (expiryParts.length > 1) {
        expiryMonth = expiryParts[0];
        expiryYear = expiryParts[1];
      }
    }
    return expiryMonth + expiryYear;
  }

  @Override
  public String toString() {
    return getPanFirst6Digits() + "******" + getPanLast4Digits();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CardToken cardToken = (CardToken) o;
    return Objects.equals(getToken(), cardToken.getToken());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getToken());
  }
}
