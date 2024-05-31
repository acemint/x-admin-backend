package com.clinic.xadmin.controller.visit;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.visit.CreateVisitResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.mapper.VisitMapper;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.visit.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = VisitControllerPath.BASE)
@Validated
public class VisitController {

  private final ControllerHelper controllerHelper;
  private final VisitService visitService;

  @Autowired
  public VisitController(ControllerHelper controllerHelper, VisitService visitService) {
    this.controllerHelper = controllerHelper;
    this.visitService = visitService;
  }

  @Operation(summary = VisitControllerDocs.CREATE_VISIT_SUMMARY, description = VisitControllerDocs.CREATE_VISIT_DESCRIPTION)
  @PostMapping(value = VisitControllerPath.CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<CreateVisitResponse>> createVisit(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid CreateVisitRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    Visit visit = this.visitService.createVisit(clinic, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<CreateVisitResponse>builder()
            .content(VisitMapper.INSTANCE.createFrom(visit))
            .build());
  }

}
