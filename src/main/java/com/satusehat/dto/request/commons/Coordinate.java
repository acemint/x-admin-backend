package com.satusehat.dto.request.commons;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Coordinate {

  private BigDecimal longitude;
  private BigDecimal latitude;
  private BigDecimal altitude;

}