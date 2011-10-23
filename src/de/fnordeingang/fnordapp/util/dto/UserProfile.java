package de.fnordeingang.fnordapp.util.dto;

import java.math.BigDecimal;

/**
 * User: vileda
 * Date: 23.10.11
 * Time: 22:27
 */
public class UserProfile {
  String name = "unknown";
  BigDecimal balance = BigDecimal.ZERO;

  public UserProfile(String name) {
    this.name = name;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getName() {
    return name;
  }
}
