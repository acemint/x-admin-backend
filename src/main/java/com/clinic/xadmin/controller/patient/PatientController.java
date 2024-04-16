package com.clinic.xadmin.controller.patient;


import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.patient.IHSCodeResponse;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;
import com.clinic.xadmin.service.patient.PatientService;
import com.clinic.xadmin.validator.annotation.ValidPatientSearchBy;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PatientControllerPath.BASE)
public class PatientController {

  private final PatientService patientService;

  @Autowired
  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @Operation(summary = PatientControllerDocs.SEARCH_PATIENT_SUMMARY, description = PatientControllerDocs.SEARCH_PATIENT_DESCRIPTION)
  @GetMapping(value = PatientControllerPath.SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StandardizedResponse<IHSCodeResponse>> searchPatientByNIK(
      @RequestParam(name = "search-by") @Valid @ValidPatientSearchBy String searchBy,
      @RequestParam(name = "nik", required = false) String nik,
      @RequestParam(name = "mother-nik", required = false) String motherNik,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "date-of-birth", required = false) String dateOfBirth,
      @RequestParam(name = "gender", required = false) String gender) {
    String ihsCode = this.patientService.getPatientFromSatuSehat(SatuSehatPatientFilter.builder()
        .searchBy(searchBy)
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
