package com.clinic.xadmin.repository.room;

import com.clinic.xadmin.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, String>, RoomCustomRepository {

  @Query(value = "SELECT CONCAT('ROM-', nextval('room_sequence'))", nativeQuery = true)
  String getNextCode();

}
