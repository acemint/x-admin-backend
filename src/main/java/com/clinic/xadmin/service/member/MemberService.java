package com.clinic.xadmin.service.member;

import com.clinic.xadmin.dto.request.member.ResetPasswordRequest;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.member.MemberFilter;
import com.clinic.xadmin.model.member.RegisterMemberData;
import org.springframework.data.domain.Page;

public interface MemberService {

  Member create(RegisterMemberData registerMemberData);
  Page<Member> get(MemberFilter memberFilter);

  Member resetPassword(ResetPasswordRequest request);

}
