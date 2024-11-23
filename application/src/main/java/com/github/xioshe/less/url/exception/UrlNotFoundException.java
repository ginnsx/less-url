package com.github.xioshe.less.url.exception;

public class UrlNotFoundException extends RuntimeException {
  public UrlNotFoundException(String message) {
    super(message);
  }

  public UrlNotFoundException() {
    super();
  }
}
