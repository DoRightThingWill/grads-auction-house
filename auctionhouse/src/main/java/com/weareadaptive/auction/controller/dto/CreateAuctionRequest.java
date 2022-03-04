package com.weareadaptive.auction.controller.dto;

public record CreateAuctionRequest (
  String symbol,
  double minPrice,
  int quantity
) {
}
