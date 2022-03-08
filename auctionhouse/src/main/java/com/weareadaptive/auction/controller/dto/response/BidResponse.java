package com.weareadaptive.auction.controller.dto.response;

public record BidResponse(
    String owner,
    int quantity,
    double price,
    String state
) {

}
