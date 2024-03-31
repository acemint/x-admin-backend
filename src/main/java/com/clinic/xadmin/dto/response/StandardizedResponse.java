package com.clinic.xadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardizedResponse<T> {

  private T content;
  private PaginationMetadata paginationMetadata;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PaginationMetadata {

    private Integer totalPages;
    private Integer currentElementSize;

  }

}
