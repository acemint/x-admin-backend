package com.clinic.xadmin.validator.annotation.visit;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidStartAndEndTime.Validator.class)
@Documented
public @interface ValidStartAndEndTime {

  String message() default "invalid start and end time combination";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidStartAndEndTime, CreateVisitRequest> {

    @Override
    public boolean isValid(CreateVisitRequest request, ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();

      if (request.getStartTime() > request.getEndTime()) {
        context.buildConstraintViolationWithTemplate("invalid payload, end time should be greater than start time")
            .addPropertyNode(CreateVisitRequest.Fields.startTime + "," + CreateVisitRequest.Fields.endTime)
            .addConstraintViolation();
        return false;
      }
      return true;
    }
  }

}

