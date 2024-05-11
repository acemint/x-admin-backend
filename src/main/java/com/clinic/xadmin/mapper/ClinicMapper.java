package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.response.clinic.ClinicResponse;
import com.clinic.xadmin.entity.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Mapper
public interface ClinicMapper {

  ClinicMapper INSTANCE = Mappers.getMapper( ClinicMapper.class );

  ClinicResponse createFrom(Clinic clinic);
  List<ClinicResponse> createFrom(List<Clinic> clinics);

  default Instant fromLocalDateTime(LocalDateTime localDateTime) {
    if (Objects.isNull(localDateTime)) {
      return null;
    }
    return localDateTime.toInstant(ZoneOffset.UTC);
  }

}
