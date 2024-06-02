package com.clinic.xadmin.constant;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstantUtils {

  public static <ANY> Set<String> extractPublicStaticFinalConstantStrings(Class<ANY> classType) {
    Field[] fields = classType.getDeclaredFields();
    return Arrays.stream(fields)
        .filter(field -> java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
            java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
            java.lang.reflect.Modifier.isFinal(field.getModifiers()) &&
            field.getType() == String.class)
        .map(f -> {
          try {
            return ((String) f.get(null));
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toSet());
    }

}
