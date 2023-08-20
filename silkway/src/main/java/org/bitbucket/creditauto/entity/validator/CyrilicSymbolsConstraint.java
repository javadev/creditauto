package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a string contains only cyrilic symbols.
 */
public class CyrilicSymbolsConstraint implements ConstraintValidator<CyrilicSymbols, String> {
    public void initialize(CyrilicSymbols constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Expected String type");
        }
        String string = (String) object;
        return string.matches("[А-Я а-яІіЇїЄє’'\\-]{1,}");
    }
}
