package com.weareadaptive.auction.controller.dto.response;
import com.weareadaptive.auction.model.AuctionLot;
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
