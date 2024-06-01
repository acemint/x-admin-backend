package com.clinic.xadmin.controller.visit;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.visit.VisitResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.mapper.VisitMapper;
import com.clinic.xadmin.model.visit.VisitFilter;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.visit.VisitService;
import com.clinic.xadmin.validator.annotation.visit.ValidVisitStatusSearchBy;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
  public ResponseEntity<StandardizedResponse<VisitResponse>> createVisit(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid CreateVisitRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    Visit visit = this.visitService.createVisit(clinic, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<VisitResponse>builder()
            .content(VisitMapper.INSTANCE.createFrom(visit))
            .build());
  }

  @Operation(summary = VisitControllerDocs.GET_LIST_VISIT_SUMMARY, description = VisitControllerDocs.GET_LIST_VISIT_DESCRIPTION)
  @GetMapping(value = VisitControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<List<VisitResponse>>> getVisit(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestParam(name = "visitStatus", defaultValue = VisitControllerDefaultValue.DEFAULT_VISIT_STATUS) @ValidVisitStatusSearchBy String visitStatus,
      @RequestParam(name = "sortBy", defaultValue = VisitControllerDefaultValue.DEFAULT_SORT_BY) String[] sortBys,
      @RequestParam(name = "sortDirection", defaultValue = VisitControllerDefaultValue.DEFAULT_SORT_DIRECTION) String sortDirection,
      @RequestParam(name = "pageNumber", defaultValue = VisitControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = VisitControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);

    VisitFilter visitFilter = VisitFilter.builder()
        .clinicCode(clinic.getCode())
        .status(visitStatus)
        .pageable(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.valueOf(sortDirection), sortBys)))
        .build();

    Page<Visit> visits = this.visitService.get(visitFilter);
    return ResponseEntity.ok().body(
        StandardizedResponse.<List<VisitResponse>>builder()
            .content(VisitMapper.INSTANCE.convertToAPIResponse(visits.getContent()))
            .paginationMetadata(PaginationMapper.INSTANCE.createFrom(visits))
            .build());
  }


}
