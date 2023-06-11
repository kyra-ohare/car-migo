package com.unosquare.carmigo.annotation;

import static com.unosquare.carmigo.constant.AppConstants.VALID_PASSWORD_MESSAGE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation checks whether a given password is valid.
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

  String message() default VALID_PASSWORD_MESSAGE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
