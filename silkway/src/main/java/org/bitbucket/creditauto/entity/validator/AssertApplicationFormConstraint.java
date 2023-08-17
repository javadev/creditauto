/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a boolean is true.
 * @author vko
 * @version $Revision$ $Date$
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
