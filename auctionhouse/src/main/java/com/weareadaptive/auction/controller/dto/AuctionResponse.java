package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.User;

import java.time.Instant;
import java.util.List;

public record AuctionResponse(int id, User owner, String symbol, double minPrice, int quantity,
                              AuctionLot.Status status, Instant closedAt, ClosingSummary closingSummary,
                              List<Bid> bids) {

  public AuctionResponse(AuctionLot auctionLot) {
    this(
      auctionLot.getId(),
      auctionLot.getOwner(),
      auctionLot.getSymbol(),
      auctionLot.getMinPrice(),
      auctionLot.getQuantity(),
      auctionLot.getStatus(),
      auctionLot.getClosingSummary().closingTime(),
      auctionLot.getClosingSummary(),
      auctionLot.getBids()
    );
  }

}
