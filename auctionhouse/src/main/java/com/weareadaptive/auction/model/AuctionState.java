package com.weareadaptive.auction.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weareadaptive.auction.controller.dto.AuctionResponse;
import org.springframework.stereotype.Component;

@Component
public class AuctionState extends State<AuctionLot> {

  private final Map<String, AuctionLot> symbol2Auction;

  public AuctionState(){
    this.symbol2Auction = new HashMap<>();
  }

  public boolean hasSymbol(String symbol){
    return symbol2Auction.containsKey(symbol);
  }

  @Override
  public void onAdd( AuctionLot auction){
    symbol2Auction.put(auction.getSymbol(), auction);
  }

  public List<LostBid> findLostBids(User user) {
    if (user == null) {
      throw new IllegalArgumentException("user cannot be null");
    }
    return stream()
        .filter(auctionLot -> AuctionLot.Status.CLOSED == auctionLot.getStatus())
        .flatMap(auctionLot -> auctionLot.getLostBids(user).stream()
            .map(b -> new LostBid(
                auctionLot.getId(),
                auctionLot.getSymbol(),
                b.getQuantity(),
                b.getPrice()))
        ).toList();
  }

  public List<WonBid> findWonBids(User user) {
    if (user == null) {
      throw new IllegalArgumentException("user cannot be null");
    }
    return stream()
        .filter(auctionLot -> AuctionLot.Status.CLOSED == auctionLot.getStatus())
        .flatMap(auctionLot -> auctionLot.getWonBids(user).stream()
            .map(winningBod -> new WonBid(
                auctionLot.getId(),
                auctionLot.getSymbol(),
                winningBod.quantity(),
                winningBod.originalBid().getQuantity(),
                winningBod.originalBid().getPrice()))
        ).toList();
  }
}
