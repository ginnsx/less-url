package com.github.xioshe.less.url.service;

public class UrlNotFoundException extends RuntimeException {
  public UrlNotFoundException(String message) {
    super(message);
  }

  public UrlNotFoundException() {
    super();
  }
}
