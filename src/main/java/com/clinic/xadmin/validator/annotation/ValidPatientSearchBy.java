package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidPatientSearchBy.Validator.class)
@Documented
public @interface ValidPatientSearchBy {

  String message() default "invalid search by: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidPatientSearchBy, String> {

    @Override
    public boolean isValid(String searchBy, ConstraintValidatorContext context) {
      if (Objects.isNull(searchBy)) {
        return true;
      }

      if (!Arrays.stream(PatientControllerSpecialValue.ALL_SEARCH_BY).toList().contains(searchBy)) {
        return false;
      }
      return true;
    }

  }

}

