package com.clinic.xadmin.constant.experimental;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Gender {

  public static final String MALE = "MALE";
  public static final String FEMALE = "FEMALE";
  public static final Set<String> VALID_GENDERS = new HashSet<>();

  static {
    Field[] fields = Gender.class.getDeclaredFields();
    for (Field field : fields) {
      if (java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
          java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
          java.lang.reflect.Modifier.isFinal(field.getModifiers()) &&
          field.getType() == String.class) {
        try {
          VALID_GENDERS.add((String) field.get(null));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
