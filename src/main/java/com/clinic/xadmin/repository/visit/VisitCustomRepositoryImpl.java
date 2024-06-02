package com.clinic.xadmin.repository.visit;


import com.clinic.xadmin.entity.QVisit;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.model.visit.VisitFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitCustomRepositoryImpl implements VisitCustomRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public Page<Visit> searchByFilter(VisitFilter filter) {
    QVisit qVisit = QVisit.visit;
    JPAQuery<Visit> visitJPAQuery = new JPAQuery<>(entityManager);

    visitJPAQuery = visitJPAQuery.select(qVisit)
        .from(qVisit)
        .where(getWhereConditions(filter));


    if (filter.getPageable().getSort().isSorted()) {
      visitJPAQuery = visitJPAQuery.orderBy(getOrderSpecifiers(filter.getPageable().getSort()).toArray(new OrderSpecifier[]{}));
    }
    if (filter.getPageable().isPaged()) {
      visitJPAQuery = visitJPAQuery.offset(filter.getPageable().getPageSize())
          .offset(filter.getPageable().getOffset());
    }

    List<Visit> visits = visitJPAQuery.fetch();
    return new PageImpl<>(visits, filter.getPageable(), visits.size());
  }

  private static BooleanExpression getWhereConditions(VisitFilter filter) {
    QVisit qVisit = QVisit.visit;

    BooleanExpression booleanExpression = qVisit.id.isNotNull();
    if (StringUtils.hasText(filter.getClinicCode())) {
      booleanExpression = booleanExpression.and(
          qVisit.practitioner.clinic.code.eq(filter.getClinicCode()));
      booleanExpression = booleanExpression.and(
          qVisit.patient.clinic.code.eq(filter.getClinicCode()));
    }
    if (StringUtils.hasText(filter.getStatus())) {
      booleanExpression = booleanExpression.and(
          qVisit.status.eq(filter.getStatus()));
    }
    return booleanExpression;
  }

  private static List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
    QVisit qVisit = QVisit.visit;
    Order orderDirection = sort.get().map(s -> s.getDirection().isAscending() ? Order.ASC : Order.DESC)
        .findFirst()
        .orElse(Order.ASC);

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (String property : sort.stream().map(Sort.Order::getProperty).toList()) {
      if (property.equals(Visit.Fields.startTime)) {
        orderSpecifiers.add(new OrderSpecifier<>(orderDirection, qVisit.startTime));
      }
    }
    return orderSpecifiers;
  }


}
