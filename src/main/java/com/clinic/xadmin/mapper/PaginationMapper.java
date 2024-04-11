package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.response.StandardizedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface PaginationMapper {

  PaginationMapper INSTANCE = Mappers.getMapper( PaginationMapper.class );

  default StandardizedResponse.PaginationMetadata createFrom(Page<?> page) {
    return StandardizedResponse.PaginationMetadata
        .builder()
        .totalPages(page.getTotalPages())
        .currentElementSize(page.getNumberOfElements())
        .build();
  }

}
