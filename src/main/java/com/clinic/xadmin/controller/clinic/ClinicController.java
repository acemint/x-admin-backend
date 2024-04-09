package com.clinic.xadmin.controller.clinic;


import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.clinic.ClinicResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.mapper.ClinicMapper;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.clinic.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ClinicControllerPath.BASE)
public class ClinicController {

  private final ClinicService clinicService;

  @Autowired
  public ClinicController(ClinicService clinicService) {
    this.clinicService = clinicService;
  }

  @Operation(
      summary = ClinicControllerDocs.REGISTER_SUMMARY,
      description = ClinicControllerDocs.REGISTER_DESCRIPTION)
  @PostMapping(value = ClinicControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_DEVELOPER)
  public ResponseEntity<StandardizedResponse<ClinicResponse>> register(@RequestBody @Valid RegisterClinicRequest request) {
    Clinic clinic = this.clinicService.createClinic(request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<ClinicResponse>builder()
            .content(ClinicMapper.INSTANCE.createFrom(clinic))
            .build());
  }

}
