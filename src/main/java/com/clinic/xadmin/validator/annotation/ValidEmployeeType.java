package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.EmployeeType;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
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
@Constraint(validatedBy = ValidEmployeeType.Validator.class)
@Documented
public @interface ValidEmployeeType {

  String message() default "employee type is unknown: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidEmployeeType, String> {

    private static final String[] VALID_EMPLOYEE_TYPE = {
        EmployeeType.DOCTOR
    };

    @Override
    public boolean isValid(String employeeType, ConstraintValidatorContext context) {
      if (Objects.isNull(employeeType)) {
        return false;
      }

      if (!Arrays.stream(VALID_EMPLOYEE_TYPE).toList().contains(employeeType)) {
        return false;
      }

      return true;
    }

  }

}

