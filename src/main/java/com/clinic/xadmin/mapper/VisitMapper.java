package com.clinic.xadmin.mapper;

import com.clinic.xadmin.constant.visit.VisitStatus;
import com.clinic.xadmin.dto.response.visit.VisitResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.entity.Room;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Mapper
public interface VisitMapper {

  VisitMapper INSTANCE = Mappers.getMapper( VisitMapper.class );

  VisitResponse createFrom(Visit visit);

  default Visit convertFromAPIRequest(Member patient, Member practitioner, Room room) {
    return Visit.builder()
        .patient(patient)
        .practitioner(practitioner)
        .room(room)
        .status(VisitStatus.PLANNED.getBackendValue())
        .startTime(LocalDateTime.now())
        .build();
  }

  default SatuSehatCreateEncounterRequest convertToSatuSehatRequest(Visit visit, Clinic clinic) {
    SatuSehatCreateEncounterRequest satuSehatCreateEncounterRequest = new SatuSehatCreateEncounterRequest();

    satuSehatCreateEncounterRequest.setStatus(VisitStatus.valueOf(visit.getStatus()).getSatuSehatValue());
    satuSehatCreateEncounterRequest.setIdentifiers(
        List.of(SatuSehatCreateEncounterRequest.EncounterIdentifier.builder()
            .system(clinic.getSatuSehatClinicReferenceId())
            .organizationId(visit.getCode())
            .build()));
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
                    .satuSehatReferenceId(ResourceType.PRACTITIONER, visit.getPractitioner().getSatuSehatPractitionerReferenceId())
                    .build())
                .participantType(
                    List.of(SatuSehatCreateEncounterRequest.ListParticipant.builder()
                        .participationType(List.of(new ParticipationType(ParticipationTypeEnum.ATND)))
                        .build())
                )
                .build()
        ));
    satuSehatCreateEncounterRequest.setPeriod(
        SatuSehatCreateEncounterRequest.Period.builder()
            .start(visit.getStartTime().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .build()
    );
    satuSehatCreateEncounterRequest.setLocations(
        Arrays.asList(SatuSehatCreateEncounterRequest.Location.builder()
            .locationData(
                Individual.builder()
                    .satuSehatReferenceId(ResourceType.LOCATION, visit.getRoom().getSatuSehatRoomReferenceId())
                    .fullName(visit.getRoom().getDescription())
                    .build())
            .build())
    );
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
