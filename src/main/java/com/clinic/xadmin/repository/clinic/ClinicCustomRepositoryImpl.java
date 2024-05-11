package com.clinic.xadmin.repository.clinic;


import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.QClinic;
import com.clinic.xadmin.model.clinic.ClinicFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.annotation.Nullable;
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
import java.util.Set;

@Repository
public class ClinicCustomRepositoryImpl implements ClinicCustomRepository {

  @Autowired
  private EntityManager entityManager;

  public static final String SORT_BY_CREATED_DATE = "createdDate";

  @Override
  public Clinic searchByName(String name) {
    QClinic qClinic = QClinic.clinic;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qClinic)
        .from(qClinic)
        .where(qClinic.name.eq(name))
        .fetchOne();
  }

  @Override
  public Clinic searchByCode(String code) {
    QClinic qClinic = QClinic.clinic;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qClinic)
        .from(qClinic)
        .where(qClinic.code.eq(code))
        .fetchOne();
  }

  @Override
  public Page<Clinic> searchByFilter(ClinicFilter clinicFilter) {
    QClinic qClinic = QClinic.clinic;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    JPAQuery<Clinic> clinicsQuery = query.select(qClinic).from(qClinic);

    // add where clause
    List<Predicate> predicates = this.getWhereClause(clinicFilter);
    if (!predicates.isEmpty()) {
      clinicsQuery = clinicsQuery.where(predicates.toArray(new Predicate[]{}));
    }

    // add limit clause
    Pageable pageable = clinicFilter.getPageable();
    clinicsQuery = clinicsQuery
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset());

    // add sort clause
    List<OrderSpecifier<?>> orderSpecifiers = this.getOrderSpecifier(pageable.getSort());
    clinicsQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[]{}));

    // extract result
    List<Clinic> result = clinicsQuery.fetch();
    return new PageImpl<>(result, clinicFilter.getPageable(), result.size());
  }

  private List<Predicate> getWhereClause(ClinicFilter filter) {
    QClinic qClinic = QClinic.clinic;

    List<Predicate> predicates = new ArrayList<>();
    if (StringUtils.hasText(filter.getKeyword())) {
      String keywordWithPercentSign = "%" + filter.getKeyword() + "%";
      predicates.add(qClinic.name.likeIgnoreCase(keywordWithPercentSign));
    }
    return predicates;
  }

  private List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
    QClinic qClinic = QClinic.clinic;
    Order orderDirection = sort.get().map(o -> o.getDirection().isAscending() ? Order.ASC : Order.DESC)
        .findFirst()
        .orElse(Order.ASC);

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (String property :  sort.stream().map(Sort.Order::getProperty).toList()) {
      if (property.equals(SORT_BY_CREATED_DATE)) {
        orderSpecifiers.add(new OrderSpecifier<>(orderDirection, qClinic.createdDate));
        continue;
      }
    }
    return orderSpecifiers;
  }

}
