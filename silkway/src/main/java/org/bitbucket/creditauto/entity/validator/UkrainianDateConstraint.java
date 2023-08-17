package org.bitbucket.creditauto.entity.validator;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UkrainianDateConstraint implements ConstraintValidator<UkrainianDate, Date> {
    public void initialize(UkrainianDate constraintAnnotation) {
    }

    public boolean isValid(Date object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if ( !( object instanceof Date ) ) {
            throw new IllegalArgumentException("Expected Date type");
        }
        Date date = (Date) object;
        return date.after(new GregorianCalendar(1991,11,31).getTime());
    }
}
