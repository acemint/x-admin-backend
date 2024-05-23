package com.clinic.xadmin.mapper;

import com.clinic.xadmin.constant.visit.VisitStatus;
import com.clinic.xadmin.dto.response.visit.CreateVisitResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.entity.Visit;
import com.satusehat.constant.ResourceType;
import com.satusehat.constant.coding.ActCode;
import com.satusehat.constant.coding.ActCodeEnum;
import com.satusehat.constant.coding.ParticipationType;
import com.satusehat.constant.coding.ParticipationTypeEnum;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.encounter.SatuSehatCreateEncounterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Mapper
public interface VisitMapper {

  VisitMapper INSTANCE = Mappers.getMapper( VisitMapper.class );

  CreateVisitResponse createFrom(Visit visit);

  default Visit convertFromAPIRequest(Member patient, Member practitioner) {
    return Visit.builder()
        .patient(patient)
        .practitioner(practitioner)
        .status(VisitStatus.NOT_STARTED.getBackendValue())
        .startTime(LocalDateTime.now())
        .build();
  }

  default SatuSehatCreateEncounterRequest convertToSatuSehatRequest(Visit visit, Clinic clinic) {
    SatuSehatCreateEncounterRequest satuSehatCreateEncounterRequest = new SatuSehatCreateEncounterRequest();

    satuSehatCreateEncounterRequest.setStatus(VisitStatus.valueOf(visit.getStatus()).getSatuSehatValue());
    satuSehatCreateEncounterRequest.setIdentifier(
        SatuSehatCreateEncounterRequest.EncounterIdentifier.builder()
            .system(clinic.getSatuSehatClinicReferenceId())
            .organizationId(visit.getPatient().getSatuSehatPatientReferenceId())
            .build());
    satuSehatCreateEncounterRequest.setActCode(new ActCode(ActCodeEnum.AMB));
    satuSehatCreateEncounterRequest.setPatient(
        Individual.builder()
            .fullName(visit.getPatient().getFullName())
            .satuSehatReferenceId(ResourceType.PATIENT, visit.getPatient().getSatuSehatPatientReferenceId())
            .build()
    );
    satuSehatCreateEncounterRequest.setParticipants(
        Arrays.asList(
            SatuSehatCreateEncounterRequest.Participant.builder()
                .individual(Individual.builder()
                    .fullName(visit.getPractitioner().getFullName())
                    .satuSehatReferenceId(ResourceType.PRACTITIONER, visit.getPractitioner().getSatuSehatPatientReferenceId())
                    .build())
                .participantType(
                    Collections.singletonList(new ParticipationType(ParticipationTypeEnum.ATND))
                )
                .build()
        ));
    satuSehatCreateEncounterRequest.setPeriod(
        SatuSehatCreateEncounterRequest.Period.builder()
            .start(visit.getStartTime())
            .build()
    );
    satuSehatCreateEncounterRequest.setLocations(null);
    satuSehatCreateEncounterRequest.setStatusHistory(
        Arrays.asList(
            SatuSehatCreateEncounterRequest.StatusHistory.builder()
                .status(satuSehatCreateEncounterRequest.getStatus())
                .period(satuSehatCreateEncounterRequest.getPeriod())
                .build())
    );
    satuSehatCreateEncounterRequest.setServiceProvider(
        Individual.builder()
            .satuSehatReferenceId(ResourceType.ORGANIZATION, clinic.getSatuSehatClinicReferenceId())
            .build()
    );
    return satuSehatCreateEncounterRequest;

  }

}
