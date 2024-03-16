package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.entity.QEmployee;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public Employee findEmployeeByEmailAddress(String emailAddress) {
    QEmployee employee = QEmployee.employee;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(employee)
        .from(employee)
        .where(employee.emailAddress.eq(emailAddress))
        .fetchOne();
  }
}
