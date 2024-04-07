package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.patient.RegisterPatientRequest;
import com.clinic.xadmin.dto.response.patient.PatientResponse;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.RegisterPatientData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PatientMapper {

  PatientMapper INSTANCE = Mappers.getMapper( PatientMapper.class );

  PatientResponse createFrom(Patient patients);
  List<PatientResponse> createFrom(List<Patient> patients);
  Patient createFrom(RegisterPatientData registerPatientData);

  RegisterPatientData convertFromDtoToModel(RegisterPatientRequest registerPatientRequest);

}
