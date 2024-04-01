package com.clinic.xadmin.controller.patient;


import com.clinic.xadmin.controller.constant.SecurityAuthorizationType;
import com.clinic.xadmin.controller.employee.EmployeeControllerDefaultValue;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.patient.PatientResponse;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.mapper.PatientResponseMapper;
import com.clinic.xadmin.model.patient.PatientFilter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = PatientControllerPath.BASE)
public class PatientController {

  private final PatientService patientService;

  @Autowired
  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  // TODO: Add integration test for this class
  @Operation(
      summary = PatientControllerDocs.GET_PATIENTS_SUMMARY,
      description = PatientControllerDocs.GET_PATIENTS_DESCRIPTION)
  @GetMapping(value = PatientControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_ADMIN_OR_DEVELOPER)
  public ResponseEntity<StandardizedResponse<List<PatientResponse>>> getPatient(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String[] sortBy,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_SORT_ORDER) String sortDirection,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    if (!Objects.isNull(sortBy)) {
      pageRequest.withSort(Sort.Direction.valueOf(sortDirection), sortBy);
    }
    PatientFilter filter = PatientFilter.builder()
        .name(name)
        .pageable(pageRequest)
        .build();

    Page<Patient> patients = patientService.getPatients(filter);
    return ResponseEntity.ok().body(
        StandardizedResponse.<List<PatientResponse>>builder()
            .content(PatientResponseMapper.INSTANCE.createFrom(patients.getContent()))
            .paginationMetadata(PaginationMapper.INSTANCE.createFrom(patients))
            .build());
  }


}
