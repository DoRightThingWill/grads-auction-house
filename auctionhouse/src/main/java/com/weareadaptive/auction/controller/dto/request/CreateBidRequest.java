package com.weareadaptive.auction.controller.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CreateBidRequest(

    @Size(max = 30)
    String owner,

    @Max(300)
    @Positive
    int quantity,

    @Positive
    double price
) {
}
