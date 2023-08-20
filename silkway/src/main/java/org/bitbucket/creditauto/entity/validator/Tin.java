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
@Constraint(validatedBy = TinConstraint.class)
@Documented
public @interface Tin {
    String message() default "{org.bitbucket.creditauto.entity.validator.tin}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
