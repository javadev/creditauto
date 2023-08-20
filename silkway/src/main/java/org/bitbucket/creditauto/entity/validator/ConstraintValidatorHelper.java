/*
 * $Id$
 */
package org.bitbucket.creditauto.entity.validator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.bitbucket.creditauto.LOG;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public abstract class ConstraintValidatorHelper {

    public static <T> T getPropertyValue(Class<T> requiredType, String propertyName, Object instance) {
        if (requiredType == null) {
            throw new IllegalArgumentException("Invalid argument. requiredType must NOT be null!");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Invalid argument. PropertyName must NOT be null!");
        }
        if (instance == null) {
            throw new IllegalArgumentException("Invalid argument. Object instance must NOT be null!");
        }
        T returnValue = null;
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, instance.getClass());
            Method readMethod = descriptor.getReadMethod();
            if (readMethod == null) {
                throw new IllegalStateException("Property '" + propertyName + "' of "
                        + instance.getClass().getName() + " is NOT readable!");
            }
            if (requiredType.isAssignableFrom(readMethod.getReturnType())) {
                try {
                    Object propertyValue = readMethod.invoke(instance);
                    returnValue = requiredType.cast(propertyValue);
                } catch (Exception e) {
                    // unable to invoke readMethod
                    LOG.error(null, e, e.getMessage());
                }
            }
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Property '" + propertyName + "' is NOT defined in "
                    + instance.getClass().getName() + "!", e);
        }
        return returnValue;
    }
}
