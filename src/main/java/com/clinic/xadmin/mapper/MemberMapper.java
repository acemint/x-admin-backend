package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsPractitionerRequest;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.entity.Member;
import com.satusehat.constant.KemkesURL;
import com.satusehat.dto.request.patient.SatuSehatCreatePatientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface MemberMapper {

  MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

  MemberResponse convertToAPIResponse(Member member);
  List<MemberResponse> convertToAPIResponse(List<Member> members);


  @Mapping(source = "status", target = "activationStatus")
  Member convertFromAPIRequest(RegisterMemberAsManagerRequest request);

  @Mapping(source = "dateOfBirth", target = "dateOfBirth", qualifiedByName = "dateOfBirth")
  @Mapping(source = "status", target = "activationStatus")
  Member convertFromAPIRequest(RegisterMemberAsPatientRequest request);

  @Mapping(source = "status", target = "activationStatus")
  Member convertFromAPIRequest(RegisterMemberAsPractitionerRequest request);

  @Named("dateOfBirth")
  default LocalDate dateStringToDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(date, formatter);
  }

  @Named("dateOfBirth")
  default String dateToDateString(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return date.format(formatter);
  }


  default SatuSehatCreatePatientRequest convertToSatuSehatAPIRequest(Member member) {
    SatuSehatCreatePatientRequest satuSehatCreatePatientRequest = new SatuSehatCreatePatientRequest();

    if (StringUtils.hasText(member.getNik())) {
      satuSehatCreatePatientRequest.getIdentifier().add(
          SatuSehatCreatePatientRequest.Identifier.builder()
              .use("official")
              .system(KemkesURL.Identity.NIK)
              .value(member.getNik())
              .build()
      );
    }

    if (StringUtils.hasText(member.getMotherNik())) {
      satuSehatCreatePatientRequest.getIdentifier().add(
          SatuSehatCreatePatientRequest.Identifier.builder()
              .use("official")
              .system(KemkesURL.Identity.MOTHER_NIK)
              .value(member.getMotherNik())
              .build()
      );
    }

    satuSehatCreatePatientRequest.getName().add(
        SatuSehatCreatePatientRequest.Name.builder()
            .use("official")
            .text(member.getFullName())
            .build()
    );

    satuSehatCreatePatientRequest.setGender(member.getGender().toLowerCase());
    satuSehatCreatePatientRequest.setDateOfBirth(dateToDateString(member.getDateOfBirth()));
    satuSehatCreatePatientRequest.setIsDeceased(Boolean.FALSE);

    if (StringUtils.hasText(member.getAddress())) {
      satuSehatCreatePatientRequest.getAddress().add(
          SatuSehatCreatePatientRequest.Address.builder()
              .use("home")
              .line(List.of(member.getAddress()))
              .build());
    }

    satuSehatCreatePatientRequest.getTelecommunications().add(
        SatuSehatCreatePatientRequest.Telecommunication
            .builder()
            .system("email")
            .value(member.getEmailAddress())
            .use("home")
            .build()
    );

    if (StringUtils.hasText(member.getPhoneNumber())) {
      satuSehatCreatePatientRequest.getTelecommunications().add(
          SatuSehatCreatePatientRequest.Telecommunication
              .builder()
              .system("phone")
              .value("+" + member.getPhoneNumber())
              .use("home")
              .build());
    }

    return satuSehatCreatePatientRequest;
  }

}
