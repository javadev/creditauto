/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class AgeMoreThan16Validator extends AbstractFormValidator {
    private static final long serialVersionUID = 1L;
    private static final int MIN_AGE = 16;
    /** form components to be validated. */
    private final FormComponent[] components;

    /**
     * Constructor.
     * @param f1 the component1 (birthday date)
     * @param f2 the component2 (doc issuer date)
     */
    public AgeMoreThan16Validator(FormComponent f1, FormComponent f2) {
        if (f1 == null) {
            throw new IllegalArgumentException("FormComponent1 cannot be null");
        }
        if (f2 == null) {
            throw new IllegalArgumentException("FormComponent2 cannot be null");
        }
        components = new FormComponent[] { f1, f2 };
    }

    /**
     * getDependentFormComponents.
     * @return the components
     */
    public FormComponent[] getDependentFormComponents() {
        return components.clone();
    }

    /**
     * Validates the form.
     * @param form the form
     */
    public void validate(Form form) {
        final Date date1 = ((TextField<Date>) components[0]).getConvertedInput();
        TextField<Date> component2 = (TextField<Date>) components[1];
        final Date date2 = component2.getConvertedInput();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        if (date1 != null && date2 != null) {
            calendar1.setTime(date1);
            calendar2.setTime(date2);
            boolean isValid = yearsBetween(calendar1, calendar2) >= MIN_AGE;
            if (!isValid) {
                error(component2);
            }
        }
    }

    private int yearsBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        int yearBetween = 0;
        date.add(Calendar.YEAR, 1);
        date.add(Calendar.DAY_OF_MONTH, -1);
        while (date.before(endDate)) {
            date.add(Calendar.YEAR, 1);
            yearBetween++;
        }
        return yearBetween;
    }
}
