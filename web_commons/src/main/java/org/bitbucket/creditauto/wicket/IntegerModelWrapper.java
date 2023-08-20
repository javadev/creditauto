package org.bitbucket.creditauto.wicket;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * .
 */
public class IntegerModelWrapper implements IWrapModel {

    private IModel wrappedModel;

    public IntegerModelWrapper(IModel wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    public IModel getWrappedModel() {
        return wrappedModel;
    }

    public Object getObject() {
        return wrappedModel.getObject() == null
                ? null
                : ((Integer) wrappedModel.getObject() < 10 ? "0" : "")
                        + String.valueOf(wrappedModel.getObject());
    }

    public void setObject(Object object) {
        String value = (String) object;
        if (value != null) {
            wrappedModel.setObject(Integer.valueOf(value));
        }
    }

    public void detach() {
        wrappedModel.detach();
    }
}
