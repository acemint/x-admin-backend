package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.employee.EmployeeType;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.util.StringUtils;

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
@Constraint(validatedBy = ValidateFieldsForDoctorType.Validator.class)
@Documented
public @interface ValidateFieldsForDoctorType {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidateFieldsForDoctorType, RegisterEmployeeRequest> {

    private static final String[] DOCTOR_ROLES = {
        EmployeeType.DOCTOR,
        EmployeeType.SPECIALIST_DOCTOR
    };

    @Override
    public boolean isValid(RegisterEmployeeRequest registerEmployeeRequest,
        ConstraintValidatorContext constraintValidatorContext) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      boolean isValid = true;
      if (Objects.isNull(registerEmployeeRequest.getType())) {
        return true;
      }
      if (!Arrays.asList(DOCTOR_ROLES).contains(registerEmployeeRequest.getType())) {
        return true;
      }
      if (!StringUtils.hasText(registerEmployeeRequest.getDoctorNumber())) {
        isValid = false;
        add(constraintValidatorContext, RegisterEmployeeRequest.Fields.doctorNumber);
      }
      if (!StringUtils.hasText(registerEmployeeRequest.getPracticeLicense())) {
        isValid = false;
        add(constraintValidatorContext, RegisterEmployeeRequest.Fields.practiceLicense);
      }
      return isValid;
    }

    private void add(ConstraintValidatorContext constraintValidatorContext, String propertyNode) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
              constraintValidatorContext.getDefaultConstraintMessageTemplate())
          .addPropertyNode(propertyNode)
          .addConstraintViolation();
    }

  }


}

