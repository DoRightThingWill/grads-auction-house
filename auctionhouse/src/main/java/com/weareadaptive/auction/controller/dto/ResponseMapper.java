package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.controller.dto.response.BidResponse;
import com.weareadaptive.auction.model.Bid;

public class ResponseMapper {

  public static BidResponse bidResponse (Bid bid){
    return new BidResponse(
        bid.getUser().getUsername(),
        bid.getQuantity(),
        bid.getPrice(),
        bid.getState().toString()
    );
  }
}
