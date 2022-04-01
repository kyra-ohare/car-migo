package com.unosquare.carmigo.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword
{
    String message() default "Password must contain between 8 and 20 characters and at least 2 of the following: " +
            "Alphanumeric characters, one special character ( @#$%^&+=!? ), one capital letter.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
