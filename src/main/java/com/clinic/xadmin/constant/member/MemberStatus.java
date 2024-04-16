package com.clinic.xadmin.constant.member;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MemberStatus {

  public static final String ACTIVE = "ACTIVE";
  public static final String INACTIVE = "INACTIVE";

  public static final Set<String> VALID_STATUS = new HashSet<>();

  static {
    Field[] fields = MemberStatus.class.getDeclaredFields();
    for (Field field : fields) {
      if (java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
          java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
          java.lang.reflect.Modifier.isFinal(field.getModifiers()) &&
          field.getType() == String.class) {
        try {
          VALID_STATUS.add((String) field.get(null));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
