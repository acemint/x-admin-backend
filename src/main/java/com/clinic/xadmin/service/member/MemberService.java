package com.clinic.xadmin.service.member;

import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.dto.request.member.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.member.MemberFilter;
import org.springframework.data.domain.Page;

public interface MemberService {

  Member create(Clinic clinic, RegisterMemberAsManagerRequest request);

  Member create(Clinic clinic, RegisterMemberAsPatientRequest request);

  Member create(Clinic clinic, RegisterMemberAsPractitionerRequest request);

  Page<Member> get(MemberFilter memberFilter);

  Member resetPassword(ResetPasswordRequest request);

  @Deprecated
  void fallbackRefetchIHSCode();

}
