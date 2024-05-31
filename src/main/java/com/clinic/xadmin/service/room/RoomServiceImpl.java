package com.clinic.xadmin.service.room;

import com.clinic.xadmin.dto.request.room.CreateRoomRequest;
import com.clinic.xadmin.entity.BaseEntity;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Room;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.RoomMapper;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.clinic.xadmin.repository.room.RoomRepository;
import com.satusehat.dto.request.location.SatuSehatCreateLocationRequest;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.location.SatuSehatCreateLocationResponse;
import com.satusehat.dto.response.location.SatuSehatSearchLocationResponse;
import com.satusehat.endpoint.location.SatuSehatCreateLocationEndpoint;
import com.satusehat.endpoint.location.SatuSehatSearchLocationsAsRoomsEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

  private final SatuSehatAPICallWrapper apiCallWrapper;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;
  private final RoomRepository roomRepository;


  @Autowired
  public RoomServiceImpl(SatuSehatAPICallWrapper apiCallWrapper, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository,
      RoomRepository roomRepository) {
    this.apiCallWrapper = apiCallWrapper;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
    this.roomRepository = roomRepository;
  }

  @Override
  public Room createRoom(Clinic clinic, CreateRoomRequest request) {
    updateRoomsFromSatuSehatInstance(clinic);
    // Create new in DB if not exist

    Room existingRoom = this.roomRepository.searchByClinicCodeAndName(clinic.getCode(), request.getName());
    if (Objects.nonNull(existingRoom)){
      throw new XAdminBadRequestException("Existing room with name " + request.getName() + " has existed");
    }
    Room room = RoomMapper.INSTANCE.convertFromAPIRequest(request);
    String satuSehatReferenceId = this.callPOSTLocation(room, clinic);
    room.setSatuSehatRoomReferenceId(satuSehatReferenceId);
    existingRoom = this.roomRepository.save(room);

    return existingRoom;
  }

  @Override
  public List<Room> getRooms(Clinic clinic) {
    updateRoomsFromSatuSehatInstance(clinic);

    return Optional.ofNullable(this.roomRepository.searchByClinicCode(clinic.getCode())).orElse(List.of());
  }

  @Override
  public Room getFirstRoom(Clinic clinic) {
    List<Room> rooms = this.roomRepository.searchByClinicCode(clinic.getCode());
    if (Objects.isNull(rooms) || rooms.isEmpty()) {
      return null;
    }
    rooms.sort(Comparator.comparing(Room::getCreatedDate));
    return rooms.getFirst();
  }

  private void updateRoomsFromSatuSehatInstance(Clinic clinic) {
    // Insert all rooms existing in SatuSehat
    List<Room> existingSatuSehatRooms = this.callGETLocationAsRooms(clinic);
    List<Room> roomsNotInXAdmin = existingSatuSehatRooms.stream()
        .filter(r -> Objects.isNull(this.roomRepository.searchByClinicCodeAndName(clinic.getCode(), r.getName())))
        .map(r -> {
          r.setCode(this.roomRepository.getNextCode());
          r.setClinic(clinic);
          return r;
        })
        .toList();
    this.roomRepository.saveAll(roomsNotInXAdmin);
  }

  private String callPOSTLocation(Room room, Clinic clinic) throws HttpStatusCodeException {
    SatuSehatCreateLocationRequest request = RoomMapper.INSTANCE.convertToSatuSehatAPIRequest(room, clinic);
    SatuSehatCreateLocationEndpoint endpoint = SatuSehatCreateLocationEndpoint.builder()
        .requestBody(request)
        .build();
    ResponseEntity<SatuSehatCreateLocationResponse> response = this.apiCallWrapper.call(endpoint, clinic.getCode());
    return response.getBody().getId();
  }

  private List<Room> callGETLocationAsRooms(Clinic clinic) throws HttpStatusCodeException {
    SatuSehatSearchLocationsAsRoomsEndpoint endpoint = SatuSehatSearchLocationsAsRoomsEndpoint.builder()
        .organizationId(clinic.getSatuSehatClinicReferenceId())
        .build();
    ResponseEntity<StandardizedResourceResponse<SatuSehatSearchLocationResponse>> response = this.apiCallWrapper.call(endpoint, clinic.getCode());
    if (Objects.isNull(response.getBody().getEntries())) {
      return null;
    }
    List<Room> rooms = response.getBody().getEntries().stream()
        .map(StandardizedResourceResponse.Entry::getResource)
        .map(RoomMapper.INSTANCE::convertFromSatuSehatAPIResponse)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return Optional.ofNullable(rooms).orElse(List.of());
  }



}
