package com.clinic.xadmin.repository.room;


import com.clinic.xadmin.entity.QRoom;
import com.clinic.xadmin.entity.Room;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomCustomRepositoryImpl implements RoomCustomRepository {

  @Autowired
  private EntityManager entityManager;


  @Override
  public List<Room> searchByClinicCode(String clinicCode) {
    QRoom qRoom = QRoom.room;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qRoom)
        .from(qRoom)
        .where(qRoom.clinic.code.eq(clinicCode))
        .fetch();
  }

  @Override
  public Room searchByClinicCodeAndName(String clinicCode, String name) {
    QRoom qRoom = QRoom.room;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qRoom)
        .from(qRoom)
        .where(qRoom.name.eq(name).and(
            qRoom.clinic.code.eq(clinicCode)))
        .fetchOne();
  }
}
