package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a boolean is true.
 */
public class AssertApplicationFormConstraint implements ConstraintValidator<AssertApplicationForm, Boolean> {
    public void initialize(AssertApplicationForm constraintAnnotation) {
    }

    public boolean isValid(Boolean object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if (!(object instanceof Boolean)) {
            throw new IllegalArgumentException("Expected Boolean type");
        }
        return (Boolean) object;
    }
}
