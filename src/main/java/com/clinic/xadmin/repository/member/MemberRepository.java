package com.clinic.xadmin.repository.member;

import com.clinic.xadmin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, String>, MemberCustomRepository {

  @Query(value = "SELECT CONCAT('MBR-', nextval('member_sequence'))", nativeQuery = true)
  String getNextCode();

}
