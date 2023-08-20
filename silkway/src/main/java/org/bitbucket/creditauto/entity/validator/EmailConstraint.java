package org.bitbucket.creditauto.entity.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraint implements ConstraintValidator<Email, String> {
 
    private final static Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");
 
    public void initialize(Email constraintAnnotation) {
    }
 
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
