package com.clinic.xadmin.validator.annotation.member;

import com.clinic.xadmin.dto.request.member.RegisterMemberAsPatientRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.util.StringUtils;

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
@Constraint(validatedBy = ValidRegisterPatientNikAndMotherNik.Validator.class)
@Documented
public @interface ValidRegisterPatientNikAndMotherNik {

  String message() default "error during validating Register Member Request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class Validator implements ConstraintValidator<ValidRegisterPatientNikAndMotherNik, RegisterMemberAsPatientRequest> {

    @Override
    public boolean isValid(RegisterMemberAsPatientRequest request, ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();

      Boolean nik = StringUtils.hasText(request.getNik());
      Boolean mothersNik = StringUtils.hasText(request.getMothersNik());

      if (!(nik ^ mothersNik)) {
        context.buildConstraintViolationWithTemplate("invalid payload, should send either NIK and mother's NIK")
            .addPropertyNode(RegisterMemberAsPatientRequest.Fields.nik + "," + RegisterMemberAsPatientRequest.Fields.mothersNik)
            .addConstraintViolation();
        return false;
      }
      return true;
    }
  }

}

