package com.weareadaptive.auction.controller.dto.response;

import com.weareadaptive.auction.model.ClosingSummary;
import java.math.BigDecimal;
import java.time.Instant;

public record BriefClosingSummary(int totalSoldQuantity, BigDecimal totalRevenue,
                                  Instant closingTime) {
  public BriefClosingSummary(ClosingSummary closingSummary) {
    this(closingSummary.totalSoldQuantity(),
        closingSummary.totalRevenue(),
        closingSummary.closingTime());
  }
}
