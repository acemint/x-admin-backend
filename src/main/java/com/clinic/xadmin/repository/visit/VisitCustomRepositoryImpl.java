package com.clinic.xadmin.repository.visit;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VisitCustomRepositoryImpl implements VisitCustomRepository {

  @Autowired
  private EntityManager entityManager;

}
