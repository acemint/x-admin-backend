package com.clinic.xadmin.controller.clinic;


import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.dto.request.clinic.UpdateClinicRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.clinic.ClinicResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.mapper.ClinicMapper;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.model.clinic.ClinicFilter;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.clinic.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @Operation(
      summary = ClinicControllerDocs.EDIT_SUMMARY,
      description = ClinicControllerDocs.EDIT_DESCRIPTION)
  @PutMapping(value = ClinicControllerPath.EDIT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_DEVELOPER)
  public ResponseEntity<StandardizedResponse<ClinicResponse>> edit(
      @RequestParam(name = "clinicCode") String clinicCode,
      @RequestBody @Valid UpdateClinicRequest request) {
    Clinic clinic = this.clinicService.editClinic(clinicCode, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<ClinicResponse>builder()
            .content(ClinicMapper.INSTANCE.createFrom(clinic))
            .build());
  }

  @Operation(
      summary = ClinicControllerDocs.GET_CLINIC_SUMMARY,
      description = ClinicControllerDocs.GET_CLINIC_DESCRIPTION)
  @GetMapping(value = ClinicControllerPath.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<ClinicResponse>> getClinic(
      @RequestParam(name = "clinicCode") String clinicCode) {
    Clinic clinic = this.clinicService.getClinic(clinicCode);

    return ResponseEntity.ok().body(
        StandardizedResponse.<ClinicResponse>builder()
            .content(ClinicMapper.INSTANCE.createFrom(clinic))
            .build());
  }

  @Operation(
      summary = ClinicControllerDocs.GET_LIST_CLINIC_SUMMARY,
      description = ClinicControllerDocs.GET_LIST_CLINIC_DESCRIPTION)
  @GetMapping(value = ClinicControllerPath.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_DEVELOPER)
  public ResponseEntity<StandardizedResponse<List<ClinicResponse>>> getClinics(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "page-number", required = false, defaultValue = ClinicControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(name = "page-size", required = false, defaultValue = ClinicControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize,
      @RequestParam(name = "sort-by", required = false, defaultValue = ClinicControllerDefaultValue.DEFAULT_SORT_BY) String[] sortBys,
      @RequestParam(name = "sort-direction", required = false, defaultValue = ClinicControllerDefaultValue.DEFAULT_SORT_DIRECTION) String sortDirection) {
    ClinicFilter clinicFilter = ClinicFilter.builder()
        .keyword(keyword)
        .pageable(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.valueOf(sortDirection), sortBys)))
        .build();

    Page<Clinic> clinics = this.clinicService.getClinics(clinicFilter);
    return ResponseEntity.ok().body(
        StandardizedResponse.<List<ClinicResponse>>builder()
            .content(ClinicMapper.INSTANCE.createFrom(clinics.getContent()))
            .paginationMetadata(PaginationMapper.INSTANCE.createFrom(clinics))
            .build()
    );
  }

}
