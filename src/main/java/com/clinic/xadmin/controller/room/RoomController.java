package com.clinic.xadmin.controller.room;

import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.room.CreateRoomRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.room.RoomResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Room;
import com.clinic.xadmin.mapper.RoomMapper;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.service.room.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RoomControllerPath.BASE)
@Validated
public class RoomController {

  private final RoomService roomService;
  private final ControllerHelper controllerHelper;

  @Autowired
  public RoomController(RoomService roomService, ControllerHelper controllerHelper) {
    this.roomService = roomService;
    this.controllerHelper = controllerHelper;
  }

  @Operation(summary = RoomControllerDocs.ADD_ROOM_SUMMARY, description = RoomControllerDocs.ADD_ROOM_DESCRIPTION)
  @PostMapping(value = RoomControllerPath.ADD, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<RoomResponse>> addRoom(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid CreateRoomRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);

    Room room = this.roomService.createRoom(clinic, request);
    return ResponseEntity.ok().body(
        StandardizedResponse.<RoomResponse>builder()
            .content(RoomMapper.INSTANCE.convertToAPIResponse(room))
            .build());
  }

  @Operation(summary = RoomControllerDocs.GET_ROOMS_SUMMARY, description = RoomControllerDocs.GET_ROOMS_DESCRIPTION)
  @GetMapping(value = RoomControllerPath.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<List<RoomResponse>>> getRooms(
      @RequestParam(name = "clinicCode", required = false) String clinicCode) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);

    List<Room> rooms = this.roomService.getRooms(clinic);
    return ResponseEntity.ok().body(
        StandardizedResponse.<List<RoomResponse>>builder()
            .content(RoomMapper.INSTANCE.convertToAPIResponse(rooms))
            .build());
  }


}
