package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.member.RegisterMemberData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {

  MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

  MemberResponse createFrom(Member member);
  Member createFrom(RegisterMemberData registerMemberData);

  List<MemberResponse> createFrom(List<Member> members);

  RegisterMemberData convertFromDtoToModel(RegisterMemberRequest registerMemberRequest);

}
