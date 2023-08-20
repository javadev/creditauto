package org.bitbucket.creditauto.wicket;

import java.lang.reflect.Field;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.bitbucket.creditauto.LOG;

public final class ValidatorUtil {
    private static final class JSR303ClassValidator implements INullAcceptingValidator {
        private final Object object;
        private final Class<?> clazz;
        private final String property;

        private JSR303ClassValidator(Object object, Class<?> clazz, String property) {
            this.object = object;
            this.clazz = clazz;
            this.property = property;
        }

        public void validate(IValidatable v) {
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Object value = v.getValue();
            Object oldValue = null;
            try {
                Field fld = object.getClass().getDeclaredField(property);
                fld.setAccessible(true);
                oldValue = fld.get(object);
                fld.set(object, value);
                Set<ConstraintViolation<Object>> constraintViolations =
                        validator.validate(object, clazz);
                if (!constraintViolations.isEmpty()) {
                    String message = constraintViolations.iterator().next().getMessage();
                    v.error(new ValidationError().setMessage(message));
                }
                fld.set(object, oldValue);
            } catch (NoSuchFieldException ex) {
                LOG.error(this, ex, ex.getMessage());
            } catch (IllegalAccessException ex) {
                LOG.error(this, ex, ex.getMessage());
            }
        }
    }

    public static INullAcceptingValidator getValidator(
            final Object object, final String property, final Class<?> clazz) {
        return new JSR303ClassValidator(object, clazz, property);
    }
}
