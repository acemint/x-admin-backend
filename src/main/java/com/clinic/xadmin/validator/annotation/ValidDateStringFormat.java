package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.util.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidDateStringFormat.Validator.class)
@Documented
public @interface ValidDateStringFormat {

  String message() default "invalid date format: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidDateStringFormat, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
      if (Objects.isNull(date)) {
        return true;
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
        simpleDateFormat.parse(date);
      } catch (IllegalArgumentException | ParseException e) {
        return false;
      }
      return true;
    }
  }

}

