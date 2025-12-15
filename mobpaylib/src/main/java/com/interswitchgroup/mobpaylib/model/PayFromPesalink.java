package com.interswitchgroup.mobpaylib.model;

import androidx.databinding.BaseObservable;
import java.io.Serializable;

public class PayFromPesalink extends BaseObservable implements Serializable {
  private Bank bank;
  private String ussdCode;

  public PayFromPesalink(Bank bank) {
    this.bank = bank;
    this.setBank(bank);
  }

  public Bank getBank() {
    return bank;
  }

  public String getUssdCode() {
    return ussdCode;
  }

  public void setUssdCode(String ussdCode) {
    this.ussdCode = ussdCode;
  }

  public void setBank(Bank bank) {
    switch (bank) {
      case NCBA:
        this.ussdCode = "*654#";
        break;
      case STANBIC:
        this.ussdCode = "*208#";
        break;
      case STANDARDCHARTERED:
        this.ussdCode = "*722#";
        break;
      case COOPERATIVEBANK:
        this.ussdCode = "*667#";
        break;
      case IMBANK:
        this.ussdCode = "*458#";
        break;
      case KCBBANK:
        this.ussdCode = "*522#";
        break;

      default:
        throw new IllegalArgumentException(
            "The type selected does not have a corresponding provider set");
    }
    this.bank = bank;
  }

  public enum Bank {
    NCBA("NCBA"),
    COOPERATIVEBANK("COOPERATIVE BANK"),
    IMBANK("I&M BANK"),
    KCBBANK("KCB BANK"),
    STANBIC("STANBIC"),
    STANDARDCHARTERED("STANDARD CHARTERED");
    private final String value;

    public String getValue() {
      return value;
    }

    Bank(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
