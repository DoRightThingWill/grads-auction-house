package com.weareadaptive.auction.controller.dto;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.User;
public record AuctionResponse(int id, String owner, String symbol, double minPrice, int quantity,
                              AuctionLot.Status status) {

  public AuctionResponse(AuctionLot auctionLot) {
    this(
      auctionLot.getId(),
      auctionLot.getOwner().getUsername(),
      auctionLot.getSymbol(),
      auctionLot.getMinPrice(),
      auctionLot.getQuantity(),
      auctionLot.getStatus()
    );
  }

}
