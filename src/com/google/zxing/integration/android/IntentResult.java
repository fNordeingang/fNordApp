package com.google.zxing.integration.android;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 11:27
 */
public class IntentResult {
  String contents;
  String format;

  public IntentResult(String contents, String format) {
    this.contents = contents;
    this.format = format;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
