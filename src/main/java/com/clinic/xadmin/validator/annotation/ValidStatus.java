package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.member.MemberStatus;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidStatus.Validator.class)
@Documented
public @interface ValidStatus {

  String message() default "status is invalid: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidStatus, String> {

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
      if (Objects.isNull(status)) {
        return true;
      }

      if (!MemberStatus.VALID_STATUS.contains(status)) {
        return false;
      }
      return true;
    }

  }

}

