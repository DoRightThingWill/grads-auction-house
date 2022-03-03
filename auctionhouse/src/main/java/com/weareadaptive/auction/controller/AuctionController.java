package com.weareadaptive.auction.controller;


import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.service.AuctionLotService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

  private final AuctionLotService auctionLotService;
  public AuctionController (AuctionLotService auctionLotService){
    this.auctionLotService = auctionLotService;
  }




}
