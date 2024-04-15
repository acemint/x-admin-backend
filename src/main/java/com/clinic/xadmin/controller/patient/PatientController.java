package com.clinic.xadmin.controller.patient;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.patient.IHSCodeResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.patient.PatientService;
import com.clinic.xadmin.validator.annotation.ValidPatientSearchBy;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PatientControllerPath.BASE)
public class PatientController {

  private final ControllerHelper controllerHelper;
  private final PatientService patientService;

  @Autowired
  public PatientController(ControllerHelper controllerHelper, PatientService patientService, SatuSehatAPICallWrapper apiCallWrapper) {
    this.controllerHelper = controllerHelper;
    this.patientService = patientService;
  }

  @Operation(
      summary = PatientControllerDocs.SEARCH_PATIENT_SUMMARY,
      description = PatientControllerDocs.SEARCH_PATIENT_DESCRIPTION)
  @GetMapping(value = PatientControllerPath.SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<IHSCodeResponse>> searchPatientByNIK(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestParam(name = "search-by") @Valid @ValidPatientSearchBy String searchBy,
      @RequestParam(name = "nik", required = false) String nik,
      @RequestParam(name = "mother-nik", required = false) String motherNik,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "date-of-birth", required = false) String dateOfBirth,
      @RequestParam(name = "gender", required = false) String gender) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);

    String ihsCode = this.patientService.getPatientFromSatuSehat(clinic, SatuSehatPatientFilter.builder()
            .nik(nik)
            .motherNik(motherNik)
            .name(name)
            .dateOfBirth(dateOfBirth)
            .gender(gender)
        .build());

    return ResponseEntity.ok().body(
        StandardizedResponse.<IHSCodeResponse>builder()
            .content(IHSCodeResponse.builder().code(ihsCode).build())
            .build());
  }

}
