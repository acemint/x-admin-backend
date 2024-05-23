package com.clinic.xadmin.service.visit;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.VisitMapper;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.member.MemberRepository;
import com.clinic.xadmin.repository.visit.VisitRepository;
import com.satusehat.dto.request.encounter.SatuSehatCreateEncounterRequest;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.endpoint.encounter.SatuSehatCreateEncounterEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

  private final VisitRepository visitRepository;
  private final MemberRepository memberRepository;
  private final SatuSehatAPICallWrapper apiCallWrapper;

  @Autowired
  public VisitServiceImpl(VisitRepository visitRepository,
      MemberRepository memberRepository,
      SatuSehatAPICallWrapper satuSehatAPICallWrapper) {
    this.visitRepository = visitRepository;
    this.memberRepository = memberRepository;
    this.apiCallWrapper = satuSehatAPICallWrapper;
  }

  public Visit createVisit(Clinic clinic, CreateVisitRequest createVisitRequest) {
    Member patient = this.memberRepository.searchByClinicCodeAndCode(clinic.getCode(), createVisitRequest.getPatientCode());
    if (!StringUtils.hasText(patient.getSatuSehatPatientReferenceId())) {
      throw new XAdminBadRequestException("Unable to create visit, patient does not have IHS Code");
    }

    Member practitioner = this.memberRepository.searchByClinicCodeAndCode(clinic.getCode(), createVisitRequest.getPractitionerCode());
    if (!StringUtils.hasText(practitioner.getSatuSehatPractitionerReferenceId())) {
      throw new XAdminBadRequestException("Unable to create visit, practitioner does not have IHS Code");
    }

    Visit visit = VisitMapper.INSTANCE.convertFromAPIRequest(patient, practitioner);
    visit = this.visitRepository.save(visit);

    SatuSehatCreateEncounterRequest satuSehatCreateEncounterRequest = VisitMapper.INSTANCE.convertToSatuSehatRequest(visit, clinic);
    SatuSehatCreateEncounterEndpoint endpoint = SatuSehatCreateEncounterEndpoint.builder()
        .requestBody(satuSehatCreateEncounterRequest)
        .build();
    ResponseEntity<StandardizedResourceResponse<Object>> response = this.apiCallWrapper.call(endpoint, clinic.getCode());
    return visit;
  }

}
