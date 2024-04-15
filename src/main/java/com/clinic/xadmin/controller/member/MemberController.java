package com.clinic.xadmin.controller.member;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.model.member.MemberFilter;
import com.clinic.xadmin.model.member.RegisterMemberData;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.member.MemberService;
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

  @Operation(
      summary = MemberControllerDocs.REGISTER_SUMMARY,
      description = MemberControllerDocs.REGISTER_DESCRIPTION)
  @PostMapping(value = MemberControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<MemberResponse>> register(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid RegisterMemberRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    clinicCode = clinic.getCode();

    RegisterMemberData registerMemberData = MemberMapper.INSTANCE.convertFromDtoToModel(request);
    registerMemberData.setClinicCode(clinicCode);

    Member member = this.memberService.create(registerMemberData);
    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.createFrom(member))
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
            .content(MemberMapper.INSTANCE.createFrom(userDetails.getMember()))
            .build());
  }

  @Operation(
      summary = MemberControllerDocs.GET_MEMBER_SUMMARY,
      description = MemberControllerDocs.GET_MEMBER_DESCRIPTION)
  @GetMapping(value = MemberControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<List<MemberResponse>>> filter(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
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
        .clinicCode(clinicCode)
        .pageable(pageRequest)
        .build();
    Page<Member> members = this.memberService.get(memberFilter);

    return ResponseEntity.ok().body(StandardizedResponse
        .<List<MemberResponse>>builder()
        .content(MemberMapper.INSTANCE.createFrom(members.getContent()))
        .paginationMetadata(PaginationMapper.INSTANCE.createFrom(members))
        .build());
  }

}
