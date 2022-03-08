package com.weareadaptive.auction.controller.dto.request;

public record CreateBidRequest(
    String owner,
    int quantity,
    double price
) {
}
