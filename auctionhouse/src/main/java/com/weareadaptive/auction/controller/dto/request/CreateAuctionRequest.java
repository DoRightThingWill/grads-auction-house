package com.weareadaptive.auction.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CreateAuctionRequest(
    @NotBlank
    @Size(max = 10)
    String symbol,

    @Positive
    double minPrice,

    @Positive
    int quantity) {
}
