package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.room.CreateRoomRequest;
import com.clinic.xadmin.dto.response.room.RoomResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Room;
import com.satusehat.constant.ResourceType;
import com.satusehat.constant.coding.LocationPhysicalType;
import com.satusehat.constant.coding.LocationPhysicalTypeEnum;
import com.satusehat.dto.request.commons.Coordinate;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.location.SatuSehatCreateLocationRequest;
import com.satusehat.dto.response.location.SatuSehatSearchLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper
public interface RoomMapper {

  RoomMapper INSTANCE = Mappers.getMapper( RoomMapper.class );

  Room convertFromAPIRequest(CreateRoomRequest createRoomRequest);

  List<RoomResponse> convertToAPIResponse(List<Room> rooms);

  RoomResponse convertToAPIResponse(Room room);

  default SatuSehatCreateLocationRequest convertToSatuSehatAPIRequest(Room room, Clinic clinic) {
    SatuSehatCreateLocationRequest satuSehatCreateLocationRequest = new SatuSehatCreateLocationRequest();

    satuSehatCreateLocationRequest.setIdentifier(
        List.of(SatuSehatCreateLocationRequest.LocationIdentifier.builder()
            .system(room.getClinic().getSatuSehatClinicReferenceId())
            .locationName(room.getName())
            .build())
    );
    satuSehatCreateLocationRequest.setName(room.getName());
    satuSehatCreateLocationRequest.setDescription(satuSehatCreateLocationRequest.getDescription());
    satuSehatCreateLocationRequest.setPhysicalType(
        SatuSehatCreateLocationRequest.PhysicalType.builder().coding(
            List.of(new LocationPhysicalType(LocationPhysicalTypeEnum.RO))
        )
            .build());
    satuSehatCreateLocationRequest.setPosition(
        Coordinate.builder()
            .latitude(BigDecimal.ZERO)
            .altitude(BigDecimal.ZERO)
            .longitude(BigDecimal.ZERO)
            .build()
    );
    satuSehatCreateLocationRequest.setManagingOrganization(
        Individual.builder()
            .satuSehatReferenceId(ResourceType.ORGANIZATION, clinic.getSatuSehatClinicReferenceId())
            .build());
    return satuSehatCreateLocationRequest;
  }

  default Room convertFromSatuSehatAPIResponse(SatuSehatSearchLocationResponse response) {
    String name = null;
    try {
      List<SatuSehatSearchLocationResponse.LocationIdentifier> identifiers = response.getIdentifier();
      Optional<String> locationId = identifiers.stream()
          .map(SatuSehatSearchLocationResponse.LocationIdentifier::getLocationName)
          .filter(StringUtils::hasText)
          .findFirst();
      if (locationId.isPresent()) {
        name = locationId.get();
      }
    } catch (NullPointerException e) {
      return null;
    }

    if (!StringUtils.hasText(name)) {
      return null;
    }

    return Room.builder()
        .name(name)
        .description(response.getDescription())
        .satuSehatRoomReferenceId(response.getId())
        .build();
  }

}
