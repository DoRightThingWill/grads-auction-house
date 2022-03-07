package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.BidOnOwnAuction;
import com.weareadaptive.auction.exception.ModelNotFoundException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.KeyAlreadyExistsException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.List;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuctionLotService {
  public static final String AUCTION_LOT_ENTITY = "AuctionLot";
  private final AuctionState auctionState;
  private final UserState userState;

  public AuctionLotService(AuctionState auctionState, UserState userState) {
    this.auctionState = auctionState;
    this.userState = userState;
  }

  public AuctionLot createAuction(String username, String symbol, int quantity, double minPrice) {
    Optional<User> userOptional = userState.getByUsername(username);
    if (userOptional.isEmpty()) {
      throw new KeyAlreadyExistsException("username not valid");
    }

    if (auctionState.hasSymbol(symbol)) {
      throw new KeyAlreadyExistsException("Symbol already exists");
    }
    User currentUser = userOptional.get();
    int auctionID = auctionState.nextId();

    AuctionLot createdAuction = new AuctionLot(
        auctionID,
        currentUser,
        symbol,
        quantity,
        minPrice
    );

    auctionState.add(createdAuction);
    return createdAuction;
  }


  public AuctionLot getAuctionByID(int id) {
    Optional<AuctionLot> auctionOptional = auctionState.getModelByID(id);
    if (auctionOptional.isEmpty()) {
      throw new ModelNotFoundException();
    }
    return auctionOptional.get();
  }

  public List<AuctionLot> getAllAuctions() {
    return auctionState.stream().toList();
  }

  public Bid bidOnAuction(int auctionID, String owner, int quantity, double price) {
    AuctionLot currentAuction = getAuctionByID(auctionID);

    User bidder = userState.getByUsername(owner).get();


    if (currentAuction.getOwner().getUsername().equals(owner)) {
      throw new BidOnOwnAuction("You can not bid on your own auction");
    }


    Bid newBid = new Bid(bidder, quantity, price);
    currentAuction.bid(newBid);
    return newBid;
  }

}
