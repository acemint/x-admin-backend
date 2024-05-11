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
import com.satusehat.dto.response.patient.PatientResourceResponse;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByIHSEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final SatuSehatAPICallWrapper apiCallWrapper;

  @Autowired
  private MemberServiceImpl(MemberRepository memberRepository,
      PasswordEncoder passwordEncoder,
      SatuSehatAPICallWrapper apiCallWrapper) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.apiCallWrapper = apiCallWrapper;
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsManagerRequest request) {
    this.validatePreviousMemberDoesNotExist(clinic, request);

    Member member = MemberMapper.INSTANCE.createFrom(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);

    member.setPassword(this.passwordEncoder.encode(request.getPassword()));
    return this.memberRepository.save(member);
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsPatientRequest request) {
    this.validatePreviousMemberDoesNotExist(clinic, request);
    this.validatePatientIHSCode(clinic, request);

    Member member = MemberMapper.INSTANCE.createFrom(request);
    member.setClinicUsername(this.getValidUsername(request, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);
    member.setRole(MemberRole.ROLE_PATIENT);

    return this.memberRepository.save(member);
  }

  @Override
  public Member create(Clinic clinic, RegisterMemberAsPractitionerRequest request) {
    this.validatePreviousMemberDoesNotExist(clinic, request);
    // TODO: ADD VALIDATION FOR PRACTITIONER IHS ID

    Member member = MemberMapper.INSTANCE.createFrom(request);
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

  private void validatePreviousMemberDoesNotExist(Clinic clinic, RegisterMemberRequest request) {
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("Email has been taken");
    }
  }

  private void validatePatientIHSCode(Clinic clinic, RegisterMemberAsPatientRequest request) {
    SatuSehatSearchPatientByIHSEndpoint endpoint = SatuSehatSearchPatientByIHSEndpoint.builder().ihsCode(request.getSatuSehatPatientReferenceId()).build();
    ResponseEntity<PatientResourceResponse>
        response = this.apiCallWrapper.call(endpoint, clinic.getCode());
    if (!StringUtils.hasText(response.getBody().getId())) {
      throw new XAdminBadRequestException("Invalid IHS Code: " + request.getSatuSehatPatientReferenceId());
    }
  }

}
