package com.clinic.xadmin.repository.room;


import com.clinic.xadmin.entity.Room;

import java.util.List;

public interface RoomCustomRepository {

  //  As of now one clinic can only support one room
  List<Room> searchByClinicCode(String clinicCode);

  Room searchByClinicCodeAndName(String clinicCode, String name);

}
