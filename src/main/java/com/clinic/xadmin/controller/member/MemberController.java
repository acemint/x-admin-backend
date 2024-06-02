package com.clinic.xadmin.controller.member;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.member.FallbackRefetchIHSCodeResponse;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.model.member.MemberFilter;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.member.MemberService;
import com.clinic.xadmin.validator.annotation.member.ValidMemberRoleSearch;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = MemberControllerPath.BASE)
public class MemberController {

  // TODO: Create a PATCH API to fetch Patient IHS Code for existing Member
  // TODO: Create a PATCH API to fetch Practitioner IHS Code for existing Member

  // TODO: Create a validation in register member, such that IHS Code from searchByNik = searchByDescription

  private final ControllerHelper controllerHelper;
  private final MemberService memberService;
  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public MemberController(
      ControllerHelper controllerHelper,
      MemberService memberService,
      AppSecurityContextHolder appSecurityContextHolder) {
    this.controllerHelper = controllerHelper;
    this.memberService = memberService;
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Operation(summary = MemberControllerDocs.REGISTER_MANAGER_REQUEST, description = MemberControllerDocs.REGISTER_MANAGER_DESCRIPTION)
  @PostMapping(value = MemberControllerPath.REGISTER_MANAGER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<MemberResponse>> registerManager(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid RegisterMemberAsManagerRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    Member member = this.memberService.create(clinic, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.convertToAPIResponse(member))
            .build());
  }

  @Operation(summary = MemberControllerDocs.REGISTER_PATIENT_SUMMARY, description = MemberControllerDocs.REGISTER_PATIENT_DESCRIPTION)
  @PostMapping(value = MemberControllerPath.REGISTER_PATIENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<MemberResponse>> registerPatient(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid RegisterMemberAsPatientRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    Member member = this.memberService.create(clinic, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.convertToAPIResponse(member))
            .build());
  }

  @Operation(summary = MemberControllerDocs.REGISTER_PRACTITIONER_SUMMARY, description = MemberControllerDocs.REGISTER_PRACTITIONER_DESCRIPTION)
  @PostMapping(value = MemberControllerPath.REGISTER_PRACTITIONER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<MemberResponse>> registerPractitionerx(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid RegisterMemberAsPractitionerRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    Member member = this.memberService.create(clinic, request);

    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.convertToAPIResponse(member))
            .build());
  }

  @Operation(
      summary = MemberControllerDocs.GET_SELF_SUMMARY)
  @GetMapping(value = MemberControllerPath.SELF, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<MemberResponse>> getSelf() {
    CustomUserDetails userDetails = (CustomUserDetails) this.appSecurityContextHolder.getCurrentContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.convertToAPIResponse(userDetails.getMember()))
            .build());
  }

  @Operation(
      summary = MemberControllerDocs.GET_MEMBER_SUMMARY,
      description = MemberControllerDocs.GET_MEMBER_DESCRIPTION)
  @GetMapping(value = MemberControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<List<MemberResponse>>> filter(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "role", required = false) @ValidMemberRoleSearch String role,
      @RequestParam(name = "sortBy", required = false) String[] sortBy,
      @RequestParam(name = "sortDirection", defaultValue = MemberControllerDefaultValue.DEFAULT_SORT_ORDER) String sortDirection,
      @RequestParam(name = "pageNumber", defaultValue = MemberControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = MemberControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    clinicCode = clinic.getCode();

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    if (!Objects.isNull(sortBy)) {
      Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection), sortBy);
      pageRequest = pageRequest.withSort(sort);
    }

    MemberFilter memberFilter = MemberFilter.builder()
        .name(name)
        .role(role)
        .clinicCode(clinicCode)
        .pageable(pageRequest)
        .build();
    Page<Member> members = this.memberService.get(memberFilter);

    return ResponseEntity.ok().body(StandardizedResponse
        .<List<MemberResponse>>builder()
        .content(MemberMapper.INSTANCE.convertToAPIResponse(members.getContent()))
        .paginationMetadata(PaginationMapper.INSTANCE.createFrom(members))
        .build());
  }

  @Deprecated
  @Operation(summary = MemberControllerDocs.FALLBACK_FETCH_IHS_CODE_PATIENT_SUMMARY, description = MemberControllerDocs.FALLBACK_FETCH_IHS_CODE_PATIENT_DESCRIPTION)
  @GetMapping(value = MemberControllerPath.FALLBACK_REFETCH_IHS_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StandardizedResponse<FallbackRefetchIHSCodeResponse>> fallbackRefetchIHSCode() {
    this.memberService.fallbackRefetchIHSCode();
    return ResponseEntity.ok().body(
        StandardizedResponse.<FallbackRefetchIHSCodeResponse>builder()
            .content(FallbackRefetchIHSCodeResponse.builder()
                .success(true)
                .build())
            .build()
    );
  }

}
