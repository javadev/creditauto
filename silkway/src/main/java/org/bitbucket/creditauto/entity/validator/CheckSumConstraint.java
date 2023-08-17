/*
 * $Id$
 *
 * Copyright (c) 2011 (alisa)
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**.
 * @author alisa
 * @version $Revision$ $Date$
 */
public class CheckSumConstraint implements ConstraintValidator<CheckSum, String> {
    public void initialize(CheckSum constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Expected String type");
        }

        String string = (String) object;
        if (string.matches("\\d+")) {
            byte[] digits = {10, 5, 7, 9, 4, 6, 10, 5, 7};
            short summ = 0;
            for (byte i = 0; i < 9; i++) {
                summ += digits[i] * (string.charAt(i) - '0');
            }
            return string.matches("\\d{10}") && ((short) (string.charAt(9) - '0') == (summ % 11 % 10));
        }
        return false;
    }
}
