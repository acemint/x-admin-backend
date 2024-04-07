package com.clinic.xadmin.repository.patient;


import com.clinic.xadmin.constant.patient.PatientSortParametersConstant;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.entity.QPatient;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientCustomRepositoryImpl implements PatientCustomRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public Patient searchByClinicCodeAndEmailAddress(String clinicCode, String emailAddress) {
    QPatient qPatient = QPatient.patient;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qPatient)
        .from(qPatient)
        .where(qPatient.emailAddress.eq(emailAddress)
            .and(qPatient.clinic.code.eq(clinicCode)))
        .fetchOne();
  }

  @Override
  public Page<Patient> searchByFilter(PatientFilter filter) {
    QPatient qPatient = QPatient.patient;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    Pageable pageable = filter.getPageable();

    List<Patient> patients = query.select(qPatient)
        .from(qPatient)
        .where(this.getBooleanExpression(filter))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .orderBy(this.getOrderSpecifier(pageable.getSort()).toArray(new OrderSpecifier[]{}))
        .fetch();

    return new PageImpl<>(patients, filter.getPageable(), patients.size());
  }

  private BooleanExpression getBooleanExpression(PatientFilter filter) {
    QPatient qPatient = QPatient.patient;

    BooleanExpression booleanExpression = qPatient.id.isNotNull();
    if (StringUtils.hasText(filter.getName())) {
      String nameWithPercentSign = "%" + Optional.ofNullable(filter.getName()).orElse("") + "%";
      booleanExpression = booleanExpression.and(
          qPatient.firstName.likeIgnoreCase(nameWithPercentSign).or(qPatient.lastName.likeIgnoreCase(nameWithPercentSign))
      );
    }
    if (StringUtils.hasText(filter.getClinicCode())) {
      booleanExpression = booleanExpression.and(qPatient.clinic.code.eq(filter.getClinicCode()));
    }
    return booleanExpression;
  }

  private List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
    QPatient qPatient = QPatient.patient;

    Order order = sort.get().map(o -> o.getDirection().isAscending() ? Order.ASC : Order.DESC)
        .findFirst()
        .orElse(Order.ASC);

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (String property : sort.stream().map(Sort.Order::getProperty).toList()) {
      if (property.equals(PatientSortParametersConstant.NAME)) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qPatient.firstName));
        orderSpecifiers.add(new OrderSpecifier<>(order, qPatient.lastName));
        continue;
      }
    }
    return orderSpecifiers;
  }


}
