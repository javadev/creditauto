/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.bitbucket.creditauto.LOG;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class DateModelWrapper implements IWrapModel {

    private String template;
    private IModel wrappedModel;

    public DateModelWrapper(String template, IModel wrappedModel) {
        this.template = template;
        this.wrappedModel = wrappedModel;
    }

    public IModel getWrappedModel() {
        return wrappedModel;
    }

    public Object getObject() {
        return wrappedModel.getObject() == null
                ? null
                : new SimpleDateFormat(template).format((Date) wrappedModel.getObject());
    }

    public void setObject(Object object) {
        String value = (String) object;
        if (value != null) {
            try {
                wrappedModel.setObject(new SimpleDateFormat(template).parse(value));
            } catch (ParseException ex) {
                LOG.error(this, ex, ex.getMessage());
            }
        }
    }

    public void detach() {
        wrappedModel.detach();
    }
}
