package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Member;
import com.satusehat.constant.KemkesURL;
import com.satusehat.dto.request.patient.SatuSehatCreatePatientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface MemberMapper {

  MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

  MemberResponse createFrom(Member member);
  Member createFrom(RegisterMemberAsManagerRequest request);
  Member createFrom(RegisterMemberAsPatientRequest request);
  Member createFrom(RegisterMemberAsPractitionerRequest request);

  List<MemberResponse> createFrom(List<Member> members);

  default SatuSehatCreatePatientRequest convertTo(Member member) {
    SatuSehatCreatePatientRequest satuSehatCreatePatientRequest = new SatuSehatCreatePatientRequest();

    if (StringUtils.hasText(member.getNik())) {
      satuSehatCreatePatientRequest.getIdentifier().add(
          SatuSehatCreatePatientRequest.Identifier.builder()
              .use("official")
              .system(KemkesURL.Identity.NIK)
              .build()
      );
    }

    if (StringUtils.hasText(member.getMotherNik())) {
      satuSehatCreatePatientRequest.getIdentifier().add(
          SatuSehatCreatePatientRequest.Identifier.builder()
              .use("official")
              .system(KemkesURL.Identity.MOTHER_NIK)
              .build()
      );
    }

    satuSehatCreatePatientRequest.getName().add(
        SatuSehatCreatePatientRequest.Name.builder()
            .use("official")
            .fullName(member.getFirstName() + " " + member.getLastName())
            .build()
    );

    satuSehatCreatePatientRequest.setGender(member.getGender().toLowerCase());
    satuSehatCreatePatientRequest.setDeceasedBoolean(Boolean.FALSE);
    satuSehatCreatePatientRequest.getAddress().add(
        SatuSehatCreatePatientRequest.Address.builder()
            .use("home")
            .line(List.of(member.getAddress()))
            .build());
    satuSehatCreatePatientRequest.getTelecommunications().add(
        SatuSehatCreatePatientRequest.Telecommunication
            .builder()
            .system("email")
            .value(member.getEmailAddress())
            .use("home")
            .build()
    );
    satuSehatCreatePatientRequest.getTelecommunications().add(
        SatuSehatCreatePatientRequest.Telecommunication
            .builder()
            .system("phone")
            .value("+" + member.getPhoneNumber())
            .use("home")
            .build()
    );

    return satuSehatCreatePatientRequest;
  }

}
