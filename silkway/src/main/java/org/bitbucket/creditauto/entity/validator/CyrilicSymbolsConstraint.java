/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a string contains only cyrilic symbols.
 * @author vko
 * @version $Revision$ $Date$
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
