/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CompareNotNullOrTrueConstraint.class)
@Documented
public @interface CompareNotNullOrTrue {
    String message() default "{org.bitbucket.creditauto.entity.validator.CompareNotNullOrTrue}";
    String checkNull() default "";
    String checkTrue() default "";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
