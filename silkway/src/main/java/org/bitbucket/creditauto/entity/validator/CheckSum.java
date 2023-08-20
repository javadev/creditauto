/*
 * $Id$
 */
package org.bitbucket.creditauto.entity.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**.
 * @author alisa
 * @version $Revision$ $Date$
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckSumConstraint.class)
@Documented
public @interface CheckSum {
    String message() default "{org.bitbucket.creditauto.entity.validator.checkSum}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
