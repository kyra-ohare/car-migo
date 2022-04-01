package com.unosquare.carmigo.annotation;

import com.google.common.base.CharMatcher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>
{
    private static final CharMatcher ALPHANUMERIC_RULE = CharMatcher
            .inRange('A', 'Z')
            .or(CharMatcher.inRange('a', 'z'))
            .or(CharMatcher.inRange('0', '9'))
            .precomputed();
    private static final CharMatcher SIGNS_RULE = CharMatcher.anyOf("@#$%^&+=!?").precomputed();
    private static final CharMatcher UPPERCASE_RULE = CharMatcher.inRange('A', 'Z').precomputed();

    @Override
    public boolean isValid(final String parameter, final ConstraintValidatorContext context)
    {
        // Allow parameter to be null as admin-api allows updating users without providing a password in the request
        if (null == parameter) {
            return true;
        }
        final boolean isValid1 = ALPHANUMERIC_RULE.matchesAnyOf(parameter);
        final boolean isValid2 = SIGNS_RULE.matchesAnyOf(parameter);
        final boolean isValid3 = UPPERCASE_RULE.matchesAnyOf(parameter);
        final boolean validLength = parameter.length() >= 8 && parameter.length() <= 20;
        final boolean onlyValidChars = ALPHANUMERIC_RULE.or(SIGNS_RULE).matchesAllOf(parameter);

        return validLength && onlyValidChars && (isValid1 && isValid2 || isValid1 && isValid3 || isValid2 && isValid3);
    }
}
