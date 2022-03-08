package com.weareadaptive.auction.controller.dto.response;

import com.weareadaptive.auction.model.AuctionLot;

public record AuctionResponse(int id, String owner, String symbol, double minPrice, int quantity,
                              AuctionLot.Status status) {

}
