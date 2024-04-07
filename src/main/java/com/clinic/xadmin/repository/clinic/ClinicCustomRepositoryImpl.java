package com.clinic.xadmin.repository.clinic;


import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.QClinic;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicCustomRepositoryImpl implements ClinicCustomRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public Clinic searchByCode(String code) {
    QClinic qClinic = QClinic.clinic;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qClinic)
        .from(qClinic)
        .where(qClinic.code.eq(code))
        .fetchOne();
  }

}
