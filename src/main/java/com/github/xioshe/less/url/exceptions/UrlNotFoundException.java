package com.github.xioshe.less.url.exceptions;

public class UrlNotFoundException extends RuntimeException {
  public UrlNotFoundException(String message) {
    super(message);
  }

  public UrlNotFoundException() {
    super();
  }
}
