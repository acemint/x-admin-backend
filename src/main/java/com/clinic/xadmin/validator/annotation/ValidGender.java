package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.experimental.Gender;
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
@Constraint(validatedBy = ValidGender.Validator.class)
@Documented
public @interface ValidGender {

  String message() default "gender is invalid: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
      if (Objects.isNull(gender)) {
        return false;
      }

      if (!Gender.VALID_GENDERS.contains(gender)) {
        return false;
      }
      return true;
    }

  }

}

