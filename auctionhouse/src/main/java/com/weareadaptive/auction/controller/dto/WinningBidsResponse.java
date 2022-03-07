package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.WinningBid;

public record WinningBidsResponse (int settledQuantity, String username, int originalQuantity, double price){
  public WinningBidsResponse(WinningBid winningBid){
    this(winningBid.originalBid().getWinQuantity(),
        winningBid.originalBid().getUser().getUsername(),
        winningBid.originalBid().getQuantity(),
        winningBid.originalBid().getPrice());
  }
}
