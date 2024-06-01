package com.clinic.xadmin.service.visit;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.entity.Room;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminInternalException;
import com.clinic.xadmin.mapper.VisitMapper;
import com.clinic.xadmin.model.visit.VisitFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.member.MemberRepository;
import com.clinic.xadmin.repository.visit.VisitRepository;
import com.clinic.xadmin.service.room.RoomService;
import com.satusehat.dto.request.encounter.SatuSehatCreateEncounterRequest;
import com.satusehat.dto.response.encounter.SatuSehatCreateEncounterResponse;
import com.satusehat.endpoint.encounter.SatuSehatCreateEncounterEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

  private final VisitRepository visitRepository;
  private final MemberRepository memberRepository;
  private final RoomService roomService;
  private final SatuSehatAPICallWrapper apiCallWrapper;

  @Autowired
  public VisitServiceImpl(VisitRepository visitRepository,
      MemberRepository memberRepository,
      RoomService roomService,
      SatuSehatAPICallWrapper satuSehatAPICallWrapper) {
    this.visitRepository = visitRepository;
    this.memberRepository = memberRepository;
    this.roomService = roomService;
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

    Room room = this.roomService.getFirstRoom(clinic);
    if (Objects.isNull(room)) {
      throw new XAdminInternalException("Need to create room first before creating visit");
    }

    Visit visit = VisitMapper.INSTANCE.convertFromAPIRequest(patient, practitioner, room, createVisitRequest);
    visit.setCode(this.visitRepository.getNextCode());

//    TODO: Create an endpoint to move this commented code to another API which will be used to send to SatuSehat once the startTime has passed current timestamp
//    SatuSehatCreateEncounterRequest satuSehatCreateEncounterRequest = VisitMapper.INSTANCE.convertToSatuSehatRequest(visit, clinic);
//    SatuSehatCreateEncounterEndpoint endpoint = SatuSehatCreateEncounterEndpoint.builder()
//        .requestBody(satuSehatCreateEncounterRequest)
//        .build();
//    ResponseEntity<SatuSehatCreateEncounterResponse> response = this.apiCallWrapper.call(endpoint, clinic.getCode());
//    visit.setSatuSehatEncounterReferenceId(response.getBody().getId());
    visit = this.visitRepository.save(visit);
    return visit;
  }

  @Override
  public Page<Visit> get(VisitFilter visitFilter) {
    return this.visitRepository.searchByFilter(visitFilter);
  }


}
