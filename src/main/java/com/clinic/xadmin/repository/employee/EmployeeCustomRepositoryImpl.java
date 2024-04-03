package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.entity.QEmployee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

  //  TODO: Choose where to move this (which layer)
  public static final LinkedHashSet<String> AVAILABLE_SORTED_BY = new LinkedHashSet<>();

  static {
    AVAILABLE_SORTED_BY.add("name");
    AVAILABLE_SORTED_BY.add(Employee.Fields.type);
    AVAILABLE_SORTED_BY.add(Employee.Fields.status);
  }

  @Autowired
  private EntityManager entityManager;

  @Override
  public Employee findEmployeeByEmailAddress(String clinicId, String emailAddress) {
    QEmployee qEmployee = QEmployee.employee;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qEmployee)
        .from(qEmployee)
        .where(qEmployee.emailAddress.eq(emailAddress)
            .and(qEmployee.clinic.id.eq(clinicId)))
        .fetchOne();
  }

  @Override
  public Page<Employee> findByFilter(EmployeeFilter filter) {
    QEmployee qEmployee = QEmployee.employee;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    Pageable pageable = filter.getPageable();

    List<Employee> employees = query.select(qEmployee)
        .from(qEmployee)
        .where(this.getBooleanExpression(filter))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .orderBy(this.getOrderSpecifier(pageable.getSort()).toArray(new OrderSpecifier[]{}))
        .fetch();

    return new PageImpl<>(employees, filter.getPageable(), employees.size());
  }

  private @Nullable BooleanExpression getBooleanExpression(EmployeeFilter filter) {
    QEmployee qEmployee = QEmployee.employee;

    BooleanExpression booleanExpression = qEmployee.id.isNotNull();
    if (StringUtils.hasText(filter.getName())) {
      String nameWithPercentSign = "%" + Optional.ofNullable(filter.getName()).orElse("") + "%";
      booleanExpression = booleanExpression.and(
              qEmployee.firstName.likeIgnoreCase(nameWithPercentSign).or(qEmployee.lastName.likeIgnoreCase(nameWithPercentSign))
      );
    }
    if (StringUtils.hasText(filter.getClinicId())) {
      booleanExpression = booleanExpression.and(
          qEmployee.clinic.id.eq(filter.getClinicId()));
    }
    return booleanExpression;
  }

  private List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
    QEmployee qEmployee = QEmployee.employee;
    Order order = sort.get().map(o -> o.getDirection().isAscending() ? Order.ASC : Order.DESC)
        .findFirst()
        .orElse(Order.ASC);

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (String property : sort.stream().map(Sort.Order::getProperty).toList()) {
      if (property.equals("name")) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qEmployee.firstName));
        orderSpecifiers.add(new OrderSpecifier<>(order, qEmployee.lastName));
        continue;
      }
      if (property.equals(Employee.Fields.type)) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qEmployee.type));
        continue;
      }
      if (property.equals(Employee.Fields.status)) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qEmployee.status));
        continue;
      }
    }
    return orderSpecifiers;
  }


}
