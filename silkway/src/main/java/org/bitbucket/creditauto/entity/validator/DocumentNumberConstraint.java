/*
 * $Id$
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a string contains 2 chars and 6 digits.
 * @author vko
 * @version $Revision$ $Date$
 */
public class DocumentNumberConstraint implements ConstraintValidator<DocumentNumber, String> {
    public void initialize(DocumentNumber constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Expected String type");
        }
        String string = (String) object;
        return string.matches("[А-Яа-я][А-Яа-я]\\d{6}");
    }
}
