/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a string contains 10 digits wit addition checks.
 *
 */
public class TinConstraint implements ConstraintValidator<Tin, String> {
    public void initialize(Tin constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if ( !( object instanceof String ) ) {
            throw new IllegalArgumentException("Expected String type");
        }
        String string = (String) object;
        return string.matches("\\d{10}");
    }
}
