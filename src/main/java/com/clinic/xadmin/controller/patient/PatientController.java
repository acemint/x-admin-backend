package com.clinic.xadmin.controller.patient;


import com.clinic.xadmin.controller.employee.EmployeeControllerDefaultValue;
import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.patient.RegisterPatientRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.patient.PatientResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.mapper.PatientMapper;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.clinic.xadmin.model.patient.RegisterPatientData;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = PatientControllerPath.BASE)
public class PatientController {

  private final ControllerHelper controllerHelper;
  private final PatientService patientService;

  @Autowired
  public PatientController(ControllerHelper controllerHelper, PatientService patientService) {
    this.controllerHelper = controllerHelper;
    this.patientService = patientService;
  }

  @Operation(
      summary = PatientControllerDocs.REGISTER_SUMMARY,
      description = PatientControllerDocs.REGISTER_DESCRIPTION)
  @PostMapping(value = PatientControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<PatientResponse>> registerPatient(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody RegisterPatientRequest registerPatientRequest) {
    Clinic clinic = controllerHelper.getInjectableClinicFromAuthentication(clinicCode);
    clinicCode = clinic.getCode();

    RegisterPatientData registerPatientData = PatientMapper.INSTANCE.convertFromDtoToModel(registerPatientRequest);
    registerPatientData.setClinicCode(clinicCode);

    Patient patient = this.patientService.createPatient(registerPatientData);
    return ResponseEntity.ok().body(
        StandardizedResponse.<PatientResponse>builder()
            .content(PatientMapper.INSTANCE.createFrom(patient))
            .build());
  }


  @Operation(
      summary = PatientControllerDocs.GET_PATIENTS_SUMMARY,
      description = PatientControllerDocs.GET_PATIENTS_DESCRIPTION)
  @GetMapping(value = PatientControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<List<PatientResponse>>> getPatient(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestParam(name = "sortBy", required = false) String[] sortBy,
      @RequestParam(name = "sortDirection", defaultValue = EmployeeControllerDefaultValue.DEFAULT_SORT_ORDER) String sortDirection,
      @RequestParam(name = "pageNumber", defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    Clinic clinic = controllerHelper.getInjectableClinicFromAuthentication(clinicCode);
    clinicCode = clinic.getCode();

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    if (!Objects.isNull(sortBy)) {
      Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection), sortBy);
      pageRequest = pageRequest.withSort(sort);
    }
    PatientFilter filter = PatientFilter.builder()
        .name(name)
        .clinicCode(clinicCode)
        .pageable(pageRequest)
        .build();

    Page<Patient> patients = patientService.getPatients(filter);
    return ResponseEntity.ok().body(
        StandardizedResponse.<List<PatientResponse>>builder()
            .content(PatientMapper.INSTANCE.createFrom(patients.getContent()))
            .paginationMetadata(PaginationMapper.INSTANCE.createFrom(patients))
            .build());
  }


}
