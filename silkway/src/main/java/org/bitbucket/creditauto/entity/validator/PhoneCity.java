/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneCityConstraint.class)
@Documented
public @interface PhoneCity {
    String message() default "{org.bitbucket.creditauto.entity.validator.phoneCity}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
