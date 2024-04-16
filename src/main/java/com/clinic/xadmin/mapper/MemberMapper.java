package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {

  MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

  MemberResponse createFrom(Member member);
  Member createFrom(RegisterMemberAsManagerRequest request);
  Member createFrom(RegisterMemberAsPatientRequest request);
  Member createFrom(RegisterMemberAsPractitionerRequest request);

  List<MemberResponse> createFrom(List<Member> members);

}
