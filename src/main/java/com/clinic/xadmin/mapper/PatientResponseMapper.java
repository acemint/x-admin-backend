package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.response.patient.PatientResponse;
import com.clinic.xadmin.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PatientResponseMapper {

  PatientResponseMapper INSTANCE = Mappers.getMapper( PatientResponseMapper.class );

  List<PatientResponse> createFrom(List<Patient> patients);

}
