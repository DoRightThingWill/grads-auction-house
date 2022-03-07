package com.weareadaptive.auction.model;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}
