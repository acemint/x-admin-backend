package com.clinic.xadmin.service.member;

import com.clinic.xadmin.constant.member.MemberRole;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.dto.request.member.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.model.member.MemberFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.member.MemberRepository;
import com.clinic.xadmin.service.patient.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final SatuSehatAPICallWrapper apiCallWrapper;
  private final PatientService patientService;

  @Autowired
  public MemberServiceImpl(MemberRepository memberRepository,
      PasswordEncoder passwordEncoder,
      SatuSehatAPICallWrapper apiCallWrapper,
      PatientService patientService) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.apiCallWrapper = apiCallWrapper;
    this.patientService = patientService;
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsManagerRequest request) {
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("member taken");
    }

    Member member = MemberMapper.INSTANCE.convertFromAPIRequest(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);

    member.setPassword(this.passwordEncoder.encode(request.getPassword()));
    return this.memberRepository.save(member);
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsPatientRequest request) {
    // Create new member while also fetching its IHS Code
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("member taken");
    }

    existingMember = MemberMapper.INSTANCE.convertFromAPIRequest(request);
    existingMember.setClinicUsername(this.getValidUsername(request, clinic, null));
    existingMember.setCode(this.memberRepository.getNextCode());
    existingMember.setClinic(clinic);
    existingMember.setRole(MemberRole.ROLE_PATIENT);

    // Obtain IHS Code from SatuSehat
    try {
      String ihsCode = this.patientService.getOrCreateSatuSehatPatient(existingMember);
      existingMember.setSatuSehatPatientReferenceId(ihsCode);
    } catch (HttpStatusCodeException e) {
      log.error("Failed to fetch member id: {}", existingMember.getId(), e);
    }

    return this.memberRepository.save(existingMember);
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsPractitionerRequest request) {
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("member taken");
    }
    // TODO: ADD VALIDATION FOR PRACTITIONER IHS ID

    Member member = MemberMapper.INSTANCE.convertFromAPIRequest(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);
    member.setRole(MemberRole.ROLE_PRACTITIONER);

    return this.memberRepository.save(member);
  }

  @Override
  public Page<Member> get(MemberFilter memberFilter) {
    memberFilter.setClinicCode(memberFilter.getClinicCode());

    return this.memberRepository.searchByFilter(memberFilter);
  }

  @Override
  public Member resetPassword(ResetPasswordRequest request) {
    Member existingMember = this.memberRepository.searchByUsername(request.getUsername());

    if (Objects.isNull(existingMember)) {
      throw new XAdminBadRequestException("User not found");
    }

    if (this.passwordEncoder.matches(request.getPreviousPassword(), existingMember.getPassword())) {
      throw new XAdminBadRequestException("Previous password does not match");
    }

    existingMember.setPassword(this.passwordEncoder.encode(request.getNewPassword()));
    return this.memberRepository.save(existingMember);
  }

  @Override
  public void fallbackRefetchIHSCode() {
    MemberFilter filterMemberPatientWithNoIHSCode = MemberFilter.builder()
        .role(MemberRole.ROLE_PATIENT)
        .filterIHSCode(MemberFilter.FilterIHSCode.builder()
            .isNull(true)
            .build())
        .pageable(Pageable.unpaged())
        .build();
    Page<Member> members = this.memberRepository.searchByFilter(filterMemberPatientWithNoIHSCode);

    for (Member member : members.stream().toList()) {
      try {
        String ihsCode = this.patientService.getOrCreateSatuSehatPatient(member);
        member.setSatuSehatPatientReferenceId(ihsCode);
      } catch (HttpStatusCodeException e) {
        log.error("Failed to fetch member id: {}", member.getId(), e);
      }
    }
    return;
  }

  private String getValidUsername(RegisterMemberRequest registerMemberData, Clinic clinic, Integer additionalIndex) {
    StringBuilder username = new StringBuilder().append(registerMemberData.getFirstName().toLowerCase());
    if (!StringUtils.hasText(registerMemberData.getLastName())) {
      username.append(".").append(registerMemberData.getFirstName().toLowerCase());
    }
    if (Objects.nonNull(additionalIndex)) {
      username.append(additionalIndex);
    }
    username.append("@").append(clinic.getCode().toLowerCase().split("-")[1]);

    if (Objects.nonNull(this.memberRepository.searchByUsername(username.toString()))) {
      return this.getValidUsername(registerMemberData, clinic, Optional.ofNullable(additionalIndex).map(i -> i + 1).orElse(1));
    }
    return username.toString();
  }

}
