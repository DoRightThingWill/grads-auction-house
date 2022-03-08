package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.BidOnOwnAuction;
import com.weareadaptive.auction.exception.ModelNotFoundException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.KeyAlreadyExistsException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import com.weareadaptive.auction.model.WinningBid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public record AuctionLotService(AuctionState auctionState, UserState userState) {

  public AuctionLot createAuction(String username, String symbol, int quantity, double minPrice) {
    Optional<User> userOptional = userState.getByUsername(username);
    if (userOptional.isEmpty()) {
      throw new KeyAlreadyExistsException("username not valid");
    }

    if (auctionState.hasSymbol(symbol)) {
      throw new KeyAlreadyExistsException("Symbol already exists");
    }
    User currentUser = userOptional.get();
    int auctionId = auctionState.nextId();

    AuctionLot createdAuction = new AuctionLot(auctionId, currentUser, symbol, quantity, minPrice);

    auctionState.add(createdAuction);
    return createdAuction;
  }


  public AuctionLot getAuctionById(int id) {
    Optional<AuctionLot> auctionOptional = auctionState.getModelByID(id);
    if (auctionOptional.isEmpty()) {
      throw new ModelNotFoundException();
    }
    return auctionOptional.get();
  }

  public List<AuctionLot> getAllAuctions() {
    return auctionState.stream().toList();
  }

  public Bid bidOnAuction(int auctionId, String owner, int quantity, double price) {
    AuctionLot currentAuction = getAuctionById(auctionId);

    if (userState.getByUsername(owner).isEmpty()) {
      throw new BusinessException("can not find user by username");
    }
    User bidder = userState.getByUsername(owner).get();


    if (currentAuction.getOwner().getUsername().equals(owner)) {
      throw new BidOnOwnAuction("You can not bid on your own auction");
    }


    Bid newBid = new Bid(bidder, quantity, price);
    currentAuction.bid(newBid);
    return newBid;
  }

  public List<WinningBid> getWinningBids(int id, String username) {
    var targetAuction = getAuctionById(id);

    if (!targetAuction.getOwner().getUsername().equals(username)) {
      throw new BusinessException("can not get winning bids of auction not belonging to you");
    }

    return targetAuction.getClosingSummary().winningBids();
  }

}
