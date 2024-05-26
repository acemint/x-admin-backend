package com.clinic.xadmin.service.room;

import com.clinic.xadmin.dto.request.room.CreateRoomRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Room;

import java.util.List;

public interface RoomService {

  Room createRoom(Clinic clinic, CreateRoomRequest request);

  List<Room> getRooms(Clinic clinic);

}
