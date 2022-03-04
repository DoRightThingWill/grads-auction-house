package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.Bid;

public record BidResponse (
    String owner,
    int quantity,
    double price,
    String state
){
  public BidResponse(Bid bid){
    this(
        bid.getUser().getUsername(),
        bid.getQuantity(),
        bid.getPrice(),
        bid.getState().toString()
    );
  }
}
