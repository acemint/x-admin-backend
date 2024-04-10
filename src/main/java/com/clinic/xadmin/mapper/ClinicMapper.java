package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.response.clinic.ClinicResponse;
import com.clinic.xadmin.entity.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClinicMapper {

  ClinicMapper INSTANCE = Mappers.getMapper( ClinicMapper.class );

  ClinicResponse createFrom(Clinic clinic);

}
