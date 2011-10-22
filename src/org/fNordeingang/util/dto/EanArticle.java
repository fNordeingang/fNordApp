package org.fNordeingang.util.dto;

import java.math.BigDecimal;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 17:02
 */
public class EanArticle {
  String name;
  String description;
  String ean;
  BigDecimal price;
  Boolean found = false;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEan() {
    return ean;
  }

  public void setEan(String ean) {
    this.ean = ean;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Boolean isFound() {
    return found;
  }

  public void setFound(Boolean found) {
    this.found = found;
  }
}
