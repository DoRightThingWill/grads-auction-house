package com.weareadaptive.auction.model;

public class KeyAlreadyExistsException extends RuntimeException {
  public KeyAlreadyExistsException(String message) {
    super(message);
  }
}
