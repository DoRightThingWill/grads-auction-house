package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionResponse;
import com.weareadaptive.auction.controller.dto.BidResponse;
import com.weareadaptive.auction.controller.dto.BriefClosingSummary;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.controller.dto.CreateBidRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auctions")
@PreAuthorize("hasRole('ROLE_USER')")
public class AuctionController {

  private final AuctionLotService auctionLotService;

  public AuctionController(AuctionLotService auctionLotService) {
    this.auctionLotService = auctionLotService;
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  AuctionResponse createAuction(@RequestBody @Valid CreateAuctionRequest request,
                                Principal principal) {
    String username = principal.getName();
    var createdAuction = auctionLotService.createAuction(
        username,
        request.symbol(),
        request.quantity(),
        request.minPrice()
    );

    return new AuctionResponse(createdAuction);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  AuctionResponse getAuctionById(@PathVariable int id) {
    return new AuctionResponse(auctionLotService.getAuctionById(id));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<AuctionResponse> getAllAuctions() {
    return auctionLotService.getAllAuctions()
        .stream()
        .map(AuctionResponse::new)
        .toList();
  }

  @PostMapping("/{id}/bid")
  @ResponseStatus(HttpStatus.CREATED)
  public BidResponse bidOnAuction(@RequestBody @Valid CreateBidRequest request,
                                  @PathVariable int id) {

    Bid createdBid = auctionLotService.bidOnAuction(
        id,
        request.owner(),
        request.quantity(),
        request.price()
    );
    return new BidResponse(createdBid);
  }

  @GetMapping("/{id}/bids/get-all")
  @ResponseStatus(HttpStatus.OK)
  public List<BidResponse> getAllBidsForAnAuctions(@PathVariable int id, Principal principal) {
    String username = principal.getName();
    return auctionLotService.getAuctionById(id).getBids().stream()
        .filter(bid -> bid.getUser().getUsername().equals(username))
        .map(BidResponse::new)
        .toList();
  }

  @GetMapping("/{id}/close")
  @ResponseStatus(HttpStatus.OK)
  public BriefClosingSummary closeAuction(@PathVariable int id, Principal principal) {
    String username = principal.getName();
    AuctionLot targetAuction = auctionLotService.getAuctionById(id);
    targetAuction.close(username);
    return new BriefClosingSummary(targetAuction.getClosingSummary());
  }

}
