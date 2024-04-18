package com.clinic.xadmin.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

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
@Constraint(validatedBy = ValidPassword.Validator.class)
@Documented
public @interface ValidPassword {

  String message() default "Password does not match criteria";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
      if (Objects.isNull(password)) {
        return true;
      }

      // Password must be at least 8 characters long
      if (password.length() < 8) {
        addMessageToContext(context, "Password must be at least 8 characters long.");
        return false;
      }

      // Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character
      boolean hasUppercase = false;
      boolean hasLowercase = false;
      boolean hasDigit = false;
      boolean hasSpecialChar = false;

      for (char ch : password.toCharArray()) {
        if (Character.isUpperCase(ch)) {
          hasUppercase = true;
        } else if (Character.isLowerCase(ch)) {
          hasLowercase = true;
        } else if (Character.isDigit(ch)) {
          hasDigit = true;
        } else {
          // Assuming special characters are ASCII characters with value less than 128
          hasSpecialChar = true;
        }
      }

      if (!(hasUppercase && hasLowercase && hasDigit && hasSpecialChar)) {
        addMessageToContext(context, "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        return false;
      }

      return true;
    }

    private void addMessageToContext(ConstraintValidatorContext context, String message) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

  }
}

