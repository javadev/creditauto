/*
 * $Id$
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
@Constraint(validatedBy = PhoneMobileConstraint.class)
@Documented
public @interface PhoneMobile {
    String message() default "{org.bitbucket.creditauto.entity.validator.phoneMobile}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
