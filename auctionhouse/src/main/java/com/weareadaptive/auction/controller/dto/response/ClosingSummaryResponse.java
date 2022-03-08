package com.weareadaptive.auction.controller.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClosingSummaryResponse(List<WinningBidsResponse> winningBids, int totalSoldQuantity,
                                     BigDecimal totalRevenue,
                                     Instant closingTime) {

}
