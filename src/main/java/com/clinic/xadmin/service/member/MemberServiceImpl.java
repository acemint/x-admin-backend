package com.clinic.xadmin.service.member;

import com.clinic.xadmin.dto.request.member.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.model.member.MemberFilter;
import com.clinic.xadmin.model.member.RegisterMemberData;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final ClinicRepository clinicRepository;

  @Autowired
  private MemberServiceImpl(MemberRepository memberRepository,
      PasswordEncoder passwordEncoder,
      ClinicRepository clinicRepository) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.clinicRepository = clinicRepository;
  }

  @Override
  public Member create(RegisterMemberData registerMemberData) {
    Member existingMember = this.memberRepository.searchByClinicCodeAndEmailAddress(
        registerMemberData.getClinicCode(), registerMemberData.getEmailAddress());
    if (Objects.nonNull(existingMember)) {
      throw new XAdminBadRequestException("Email has been taken");
    }
    Clinic clinic = this.clinicRepository.searchByCode(registerMemberData.getClinicCode());

    Member member = MemberMapper.INSTANCE.createFrom(registerMemberData);
    member.setPassword(this.passwordEncoder.encode(registerMemberData.getPassword()));
    member.setUsername(this.getValidUsername(registerMemberData, clinic, null));
    member.setCode(this.memberRepository.getNextCode());
    member.setClinic(clinic);

    return this.memberRepository.save(member);
  }

  private String getValidUsername(RegisterMemberData registerMemberData, Clinic clinic, Integer additionalIndex) {
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

}
