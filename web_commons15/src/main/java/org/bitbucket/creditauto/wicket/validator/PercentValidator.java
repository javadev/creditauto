/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket.validator;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.bitbucket.creditauto.entity.Dictionary_data;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class PercentValidator extends AbstractFormValidator {
    private static final long serialVersionUID = 1L;
    /** form components to be validated. */
    private final FormComponent[] components;

    /**
     * Constructor.
     * @param f1 the component1 (percent/sum value)
     * @param f2 the component2 (price variant)
     */
    public PercentValidator(FormComponent f1, FormComponent f2) {
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
        java.math.BigDecimal percentSum = ((TextField<java.math.BigDecimal>) components[0]).getConvertedInput();
        Dictionary_data priceVariant = ((DropDownChoice<Dictionary_data>) components[1]).getConvertedInput();
        if (priceVariant != null && "1".equals(priceVariant.getDkey())) {
            if (percentSum != null && (percentSum.intValue() < 0 || percentSum.intValue() > 100)) {
                error(components[0]);
            }
        }
    }
}
