package com.weareadaptive.auction.controller;
import com.weareadaptive.auction.controller.dto.AuctionResponse;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.service.AuctionLotService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/auctions")
@PreAuthorize("hasRole('ROLE_USER')")
public class AuctionController {

  private final AuctionLotService auctionLotService;
  public AuctionController (AuctionLotService auctionLotService){
    this.auctionLotService = auctionLotService;
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  AuctionResponse createAuction(@RequestBody @Valid CreateAuctionRequest request, Principal principal){
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
  AuctionResponse getAuctionByID(@PathVariable int id){
    return new AuctionResponse(auctionLotService.getAuctionByID(id));
  }



}
