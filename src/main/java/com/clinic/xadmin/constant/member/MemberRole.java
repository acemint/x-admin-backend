package com.clinic.xadmin.constant.member;

import com.clinic.xadmin.constant.ConstantUtils;
import com.clinic.xadmin.entity.Member;

import java.util.Map;
import java.util.Set;

public class MemberRole {

  public static final String ROLE_DEVELOPER = "ROLE_DEVELOPER";
  public static final String ROLE_VENDOR = "ROLE_VENDOR";
  public static final String ROLE_CLINIC_ADMIN = "ROLE_CLINIC_ADMIN";
  public static final String ROLE_PRACTITIONER = "ROLE_PRACTITIONER";
  public static final String ROLE_PATIENT = "ROLE_PATIENT";
  public static final Set<String> ALL_ROLES = ConstantUtils.extractPublicStaticFinalConstantStrings(MemberRole.class);

  // Firefighter roles are defined as roles which does not need any clinic
  private static final Map<String, Boolean> MANAGER_ROLES = Map.ofEntries(
      Map.entry(ROLE_DEVELOPER, true),
      Map.entry(ROLE_VENDOR, true)
  );

  public static Boolean isFirefighterRoles(Member member) {
    return MemberRole.MANAGER_ROLES.containsKey(member.getRole());
  }


}
