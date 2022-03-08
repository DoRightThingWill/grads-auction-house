package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.ResponseMapper;
import com.weareadaptive.auction.controller.dto.request.CreateAuctionRequest;
import com.weareadaptive.auction.controller.dto.request.CreateBidRequest;
import com.weareadaptive.auction.controller.dto.response.AuctionResponse;
import com.weareadaptive.auction.controller.dto.response.BidResponse;
import com.weareadaptive.auction.controller.dto.response.ClosingSummaryResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    var createdAuction =
        auctionLotService.createAuction(principal.getName(), request.symbol(), request.quantity(),
            request.minPrice());

    return ResponseMapper.auctionResponse(createdAuction);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  AuctionResponse getAuctionById(@PathVariable int id) {
    return ResponseMapper.auctionResponse(auctionLotService.getAuctionById(id));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<AuctionResponse> getAllAuctions() {
    return auctionLotService.getAllAuctions().stream().map(ResponseMapper::auctionResponse)
        .toList();
  }

  @PostMapping("/{id}/bid")
  @ResponseStatus(HttpStatus.CREATED)
  public BidResponse bidOnAuction(@RequestBody @Valid CreateBidRequest request,
                                  @PathVariable int id) {

    Bid createdBid =
        auctionLotService.bidOnAuction(id, request.owner(), request.quantity(), request.price());
    return ResponseMapper.bidResponse(createdBid);
  }

  @GetMapping("/{id}/bids")
  @ResponseStatus(HttpStatus.OK)
  public List<BidResponse> getAllBidsForAnAuctions(@PathVariable int id, Principal principal) {

    return auctionLotService.getAllBidsFromAuction(principal, id).stream()
        .map(ResponseMapper::bidResponse).toList();
  }

  @PutMapping("/{id}/close")
  @ResponseStatus(HttpStatus.OK)
  public ClosingSummaryResponse closeAuction(@PathVariable int id, Principal principal) {

    AuctionLot closedAuction = auctionLotService.closeAuction(principal, id);
    return ResponseMapper.closingSummaryResponse(closedAuction.getClosingSummary());
  }


  @GetMapping("/{id}/close-summary")
  @ResponseStatus(HttpStatus.OK)
  public ClosingSummaryResponse getCloseSummary(@PathVariable int id, Principal principal) {
    return ResponseMapper.closingSummaryResponse(auctionLotService.getClosSummary(principal, id));
  }
}
