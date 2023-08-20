/*
 * $Id$
 */
package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public class CompareNotNullOrTrueConstraint implements ConstraintValidator<CompareNotNullOrTrue, Object> {
    private String checkNull;
    private String checkTrue;

    public void initialize(CompareNotNullOrTrue constraintAnnotation) {
        checkNull = constraintAnnotation.checkNull();
        checkTrue = constraintAnnotation.checkTrue();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        Object checkNullValue = ConstraintValidatorHelper.getPropertyValue(Object.class, checkNull, object);
        Boolean checkTrueValue = ConstraintValidatorHelper.getPropertyValue(Boolean.class, checkTrue, object);
        return checkNullValue != null || (checkTrueValue != null && checkTrueValue);
    }
}
