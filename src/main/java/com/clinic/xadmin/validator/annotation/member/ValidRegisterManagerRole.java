package com.clinic.xadmin.validator.annotation.member;

import com.clinic.xadmin.constant.member.MemberRole;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidRegisterManagerRole.Validator.class)
@Documented
public @interface ValidRegisterManagerRole {

  String message() default "invalid role creation: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidRegisterManagerRole, String> {

    private static final String[] VALID_ROLE_TO_REGISTER = {
        MemberRole.ROLE_CLINIC_ADMIN,
    };

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
      if (Objects.isNull(role)) {
        return true;
      }

      if (!Arrays.stream(VALID_ROLE_TO_REGISTER).toList().contains(role)) {
        return false;
      }
      return true;
    }

  }

}

