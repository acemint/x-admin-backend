package com.clinic.xadmin.validator.annotation;

import com.clinic.xadmin.constant.member.MemberType;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
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
@Constraint(validatedBy = ValidateFieldsForDoctorType.Validator.class)
@Documented
@Deprecated
public @interface ValidateFieldsForDoctorType {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidateFieldsForDoctorType, RegisterMemberRequest> {

    private static final String[] DOCTOR_ROLES = {
        MemberType.DOCTOR,
        MemberType.SPECIALIST_DOCTOR
    };

    @Override
    public boolean isValid(RegisterMemberRequest registerMemberRequest,
        ConstraintValidatorContext constraintValidatorContext) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      boolean isValid = true;
      if (Objects.isNull(registerMemberRequest.getType())) {
        return true;
      }
      if (!Arrays.asList(DOCTOR_ROLES).contains(registerMemberRequest.getType())) {
        return true;
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

