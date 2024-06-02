package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidFutureEpochAsLong.Validator.class)
@Documented
public @interface ValidFutureEpochAsLong {

  String message() default "invalid epoch timestamp, should be future date: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidFutureEpochAsLong, Long> {

    @Override
    public boolean isValid(Long epoch, ConstraintValidatorContext context) {
      if (Objects.isNull(epoch)) {
        return true;
      }

      if (epoch > 253_402_171_200L) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("invalid epoch, please pass epoch as seconds instead of epoch milliseconds: " + epoch)
            .addPropertyNode("time")
            .addConstraintViolation();
        return false;
      }

      if (LocalDateTime.now().isBefore(LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault()))) {
        return true;
      }
      return false;
    }

  }

}

