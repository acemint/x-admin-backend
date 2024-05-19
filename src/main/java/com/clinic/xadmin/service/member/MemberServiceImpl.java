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
import com.clinic.xadmin.service.practitioner.PractitionerService;
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
  private final PractitionerService practitionerService;

  @Autowired
  public MemberServiceImpl(MemberRepository memberRepository,
      PasswordEncoder passwordEncoder,
      SatuSehatAPICallWrapper apiCallWrapper,
      PatientService patientService,
      PractitionerService practitionerService) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.apiCallWrapper = apiCallWrapper;
    this.patientService = patientService;
    this.practitionerService = practitionerService;
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
    // Create new member as Patient while also fetching its IHS Code
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("member taken");
    }

    Member member = MemberMapper.INSTANCE.convertFromAPIRequest(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);
    member.setRole(MemberRole.ROLE_PATIENT);

    // Obtain IHS Code from SatuSehat
    try {
      String ihsCode = this.patientService.getOrCreateSatuSehatPatient(member);
      member.setSatuSehatPatientReferenceId(ihsCode);
    } catch (HttpStatusCodeException e) {
      log.error("Failed to fetch Patient IHS Code: {}", member.getId(), e);
    }

    return this.memberRepository.save(member);
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsPractitionerRequest request) {
    // Create new member as Practitioner while also fetching its IHS Code
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("member taken");
    }

    Member member = MemberMapper.INSTANCE.convertFromAPIRequest(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);
    member.setRole(MemberRole.ROLE_PRACTITIONER);

    // Obtain IHS Code from SatuSehat
    try {
      String ihsCode = this.practitionerService.getPractitionerFromSatuSehat(member);
      member.setSatuSehatPractitionerReferenceId(ihsCode);
    } catch (HttpStatusCodeException e) {
      log.error("Failed to fetch Practitioner IHS Code: {}", member.getId(), e);
    }

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
    Page<Member> patients = this.memberRepository.searchByFilter(filterMemberPatientWithNoIHSCode);

    for (Member member : patients.stream().toList()) {
      try {
        String ihsCode = this.patientService.getOrCreateSatuSehatPatient(member);
        member.setSatuSehatPatientReferenceId(ihsCode);
      } catch (HttpStatusCodeException e) {
        log.error("Failed to fetch Patient IHS Code: {}", member.getId(), e);
      }
    }

    MemberFilter filterMemberPractitionerWithNoIHSCode = MemberFilter.builder()
        .role(MemberRole.ROLE_PRACTITIONER)
        .filterIHSCode(MemberFilter.FilterIHSCode.builder()
            .isNull(true)
            .build())
        .pageable(Pageable.unpaged())
        .build();
    Page<Member> practitioners = this.memberRepository.searchByFilter(filterMemberPractitionerWithNoIHSCode);

    for (Member member : practitioners.stream().toList()) {
      try {
        String ihsCode = this.practitionerService.getPractitionerFromSatuSehat(member);
        member.setSatuSehatPractitionerReferenceId(ihsCode);
      } catch (HttpStatusCodeException e) {
        log.error("Failed to fetch Practitioner IHS Code: {}", member.getId(), e);
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
