package com.weareadaptive.auction.controller.dto;

public record CreateBidRequest(
    String owner,
    int quantity,
    double price
) {
}
