package org.bitbucket.creditauto.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VinConstraint implements ConstraintValidator<Vin, String> {
    public void initialize(Vin constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        if ( !( object instanceof String ) ) {
            throw new IllegalArgumentException("Expected String type");
        }
        
        String string = (String) object;

      return string.toUpperCase().matches("[[A-Z0-9]&&[^IQO]]{17}"); 
    }
}
