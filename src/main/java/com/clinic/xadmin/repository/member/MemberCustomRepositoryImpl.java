package com.clinic.xadmin.repository.member;


import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.entity.QMember;
import com.clinic.xadmin.model.member.MemberFilter;
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
import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

  //  TODO: Choose where to move this (which layer)
  public static final LinkedHashSet<String> AVAILABLE_SORTED_BY = new LinkedHashSet<>();

  static {
    AVAILABLE_SORTED_BY.add("name");
    AVAILABLE_SORTED_BY.add(Member.Fields.practitionerType);
    AVAILABLE_SORTED_BY.add(Member.Fields.activationStatus);
  }

  @Autowired
  private EntityManager entityManager;

  @Override
  public Member searchByUsername(String username) {
    QMember qMember = QMember.member;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qMember)
        .from(qMember)
        .where(qMember.clinicUsername.eq(username))
        .fetchOne();
  }

  @Override
  public Member searchByClinicCodeAndEmailAddress(String clinicCode, String emailAddress) {
    QMember qMember = QMember.member;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qMember)
        .from(qMember)
        .where(qMember.emailAddress.eq(emailAddress)
            .and(qMember.clinic.code.eq(clinicCode)))
        .fetchOne();
  }

  @Override
  public Member searchByClinicCodeAndNik(String clinicCode, String nik) {
    QMember qMember = QMember.member;
    JPAQuery<?> query = new JPAQuery<>(entityManager);

    return query.select(qMember)
        .from(qMember)
        .where(qMember.nik.eq(nik)
            .and(qMember.clinic.code.eq(clinicCode)))
        .fetchOne();
  }

  @Override
  public Page<Member> searchByFilter(MemberFilter filter) {
    QMember qMember = QMember.member;
    JPAQuery<Member> query = new JPAQuery<>(entityManager);

    Pageable pageable = filter.getPageable();

    query = query.select(qMember)
        .from(qMember)
        .where(this.getBooleanExpression(filter));

    if (!pageable.isUnpaged()) {
      query = query.limit(pageable.getPageSize())
          .offset(pageable.getOffset());
    }

    if (!pageable.getSort().isUnsorted()) {
      query = query.orderBy(this.getOrderSpecifier(pageable.getSort()).toArray(new OrderSpecifier[]{}));
    }

    List<Member> members = query.fetch();

    return new PageImpl<>(members, filter.getPageable(), members.size());
  }

  private @Nullable BooleanExpression getBooleanExpression(MemberFilter filter) {
    QMember qMember = QMember.member;

    BooleanExpression booleanExpression = qMember.id.isNotNull();
    if (StringUtils.hasText(filter.getName())) {
      String nameWithPercentSign = "%" + Optional.ofNullable(filter.getName()).orElse("") + "%";
      booleanExpression = booleanExpression.and(
              qMember.firstName.likeIgnoreCase(nameWithPercentSign).or(qMember.lastName.likeIgnoreCase(nameWithPercentSign))
      );
    }
    if (StringUtils.hasText(filter.getClinicCode())) {
      booleanExpression = booleanExpression.and(
          qMember.clinic.code.eq(filter.getClinicCode()));
    }
    if (StringUtils.hasText(filter.getRole())) {
      booleanExpression = booleanExpression.and(
          qMember.role.eq(filter.getRole()));
    }
    if (Objects.nonNull(filter.getFilterIHSCode())) {
      if (filter.getFilterIHSCode().isNull()) {
        booleanExpression = booleanExpression.and(
            qMember.satuSehatPatientReferenceId.isNull());
      }
      else {
        booleanExpression = booleanExpression.and(
            qMember.satuSehatPatientReferenceId.isNotNull());
      }
    }
    return booleanExpression;
  }

  private List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
    QMember qMember = QMember.member;
    Order order = sort.get().map(o -> o.getDirection().isAscending() ? Order.ASC : Order.DESC)
        .findFirst()
        .orElse(Order.ASC);

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (String property : sort.stream().map(Sort.Order::getProperty).toList()) {
      if (property.equals("name")) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qMember.firstName));
        orderSpecifiers.add(new OrderSpecifier<>(order, qMember.lastName));
        continue;
      }
      if (property.equals(Member.Fields.practitionerType)) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qMember.practitionerType));
        continue;
      }
      if (property.equals(Member.Fields.activationStatus)) {
        orderSpecifiers.add(new OrderSpecifier<>(order, qMember.activationStatus));
        continue;
      }
    }
    return orderSpecifiers;
  }


}
