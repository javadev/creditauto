/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.bitbucket.creditauto.LOG;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class CheckConstraint implements ConstraintValidator<Check, Object> {
    private int min;
    private int max;

    public void initialize(Check constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }

        int value = 0;
        if (object instanceof java.math.BigDecimal) {
            value = ((java.math.BigDecimal) object).intValue();
        } else if (object instanceof Integer) {
            value = (Integer) object;
        } else if (object instanceof String) {
            value = Integer.valueOf((String) object);
        } else {
            return false;
        }
        return value >= min && value <= max;
    }
}
