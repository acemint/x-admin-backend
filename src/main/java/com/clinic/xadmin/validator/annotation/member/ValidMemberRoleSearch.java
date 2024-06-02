package com.clinic.xadmin.validator.annotation.member;

import com.clinic.xadmin.constant.member.MemberRole;
import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
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
@Constraint(validatedBy = ValidMemberRoleSearch.Validator.class)
@Documented
public @interface ValidMemberRoleSearch {

  String message() default "invalid role: ${validatedValue} not found";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidMemberRoleSearch, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
      if (Objects.isNull(role)) {
        return true;
      }
      if (!MemberRole.ALL_ROLES.contains(role)) {
        return false;
      }
      return true;
    }

  }

}

