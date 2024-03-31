package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.EmployeeType;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
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
@Constraint(validatedBy = ValidDoctorNumberNotNullOnSpecificRole.Validator.class)
@Documented
public @interface ValidDoctorNumberNotNullOnSpecificRole {

  String message() default "doctor number should not be null";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidDoctorNumberNotNullOnSpecificRole, RegisterEmployeeRequest> {

    @Override
    public boolean isValid(RegisterEmployeeRequest registerEmployeeRequest,
        ConstraintValidatorContext constraintValidatorContext) {
      if (Objects.isNull(registerEmployeeRequest.getType())) {
        return true;
      }
      if (registerEmployeeRequest.getType().equals(EmployeeType.DOCTOR) &&
          Objects.isNull(registerEmployeeRequest.getDoctorNumber())) {
        return false;
      }
      return true;
    }
  }


}

