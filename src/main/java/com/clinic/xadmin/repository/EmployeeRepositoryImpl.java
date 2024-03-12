package com.clinic.xadmin.repository;

import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.entity.QEmployee;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public Employee getEmployeeById() {
    QEmployee employee = QEmployee.employee;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(employee)
        .from(employee)
        .fetchOne();
  }

}
