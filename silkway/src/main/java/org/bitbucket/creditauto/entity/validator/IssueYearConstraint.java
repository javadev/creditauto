/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class IssueYearConstraint implements ConstraintValidator<IssueYear, Object> {
    public void initialize(IssueYear constraintAnnotation) {
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if (!(object instanceof Integer)) {
            throw new IllegalArgumentException("Expected Integer type");
        }

        Integer value = (Integer) object;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        return value > 2000 && value <= year;
    }
}