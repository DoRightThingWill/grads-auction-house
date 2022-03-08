package com.weareadaptive.auction.controller.dto.request;

public record CreateAuctionRequest(
    String symbol,
    double minPrice,
    int quantity
) {
}
