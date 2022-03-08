package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.controller.dto.response.AuctionResponse;
import com.weareadaptive.auction.controller.dto.response.BidResponse;
import com.weareadaptive.auction.controller.dto.response.ClosingSummaryResponse;
import com.weareadaptive.auction.controller.dto.response.WinningBidsResponse;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.WinningBid;

public class ResponseMapper {

  public static BidResponse bidResponse(Bid bid) {
    return new BidResponse(
        bid.getUser().getUsername(),
        bid.getQuantity(),
        bid.getPrice(),
        bid.getState().toString()
    );
  }

  public static WinningBidsResponse winningBidsResponse(WinningBid winningBid) {
    return new WinningBidsResponse(
        winningBid.originalBid().getWinQuantity(),
        winningBid.originalBid().getUser().getUsername(),
        winningBid.originalBid().getQuantity(),
        winningBid.originalBid().getPrice());
  }

  public static ClosingSummaryResponse closingSummaryResponse(ClosingSummary closingSummary) {
    return new ClosingSummaryResponse(
        closingSummary.winningBids()
            .stream()
            .map(ResponseMapper::winningBidsResponse)
            .toList(),
        closingSummary.totalSoldQuantity(),
        closingSummary.totalRevenue(),
        closingSummary.closingTime()
    );
  }

  public static AuctionResponse auctionResponse(AuctionLot auctionLot) {
    return new AuctionResponse(
        auctionLot.getId(),
        auctionLot.getOwner().getUsername(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity(),
        auctionLot.getStatus()
    );
  }


}
