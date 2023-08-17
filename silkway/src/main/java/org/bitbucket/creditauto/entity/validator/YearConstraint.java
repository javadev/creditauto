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
public class YearConstraint implements ConstraintValidator<Year, Object> {
    private String min;
    private String max;

    public void initialize(Year constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }

        int year = 0;
        if (object instanceof String) {
            year = Integer.valueOf((String) object);
        } else if (object instanceof java.util.Date) {
            java.util.Date value = (java.util.Date) object;
            year = value.getYear();
        } else {
            return false;
        }
        if ("".equals(max)) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            max = String.valueOf(calendar.get(java.util.Calendar.YEAR));
        }
        return ("".equals(min) || year > Integer.valueOf(min)) && ("".equals(max) || year <= Integer.valueOf(max));
    }
}
