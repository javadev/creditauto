/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.bitbucket.creditauto.entity.Dictionary_data;
import org.apache.wicket.markup.html.form.DropDownChoice;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class DictionaryModelWrapper implements IWrapModel {

    private final IModel wrappedModel;
    private final DropDownChoice ddc;
    private final DictionaryTextField dtf;

    public DictionaryModelWrapper(IModel wrappedModel, DropDownChoice ddc) {
        this.wrappedModel = wrappedModel;
        this.ddc = ddc;
        this.dtf = null;
    }

    public DictionaryModelWrapper(IModel wrappedModel, DictionaryTextField dtf) {
        this.wrappedModel = wrappedModel;
        this.ddc = null;
        this.dtf = dtf;
    }

    public IModel getWrappedModel() {
        return wrappedModel;
    }

    public Object getObject() {
        String value = (String) wrappedModel.getObject();
        for (Dictionary_data item : ddc == null ? dtf.getChoices() : (List<Dictionary_data>) ddc.getChoices()) {
            if (item.getDkey().equals(value)) {
                return item;
            }
        }
        return null;
    }

    public void setObject(Object object) {
        String value = object == null ? null : ((Dictionary_data) object).getDkey();
        wrappedModel.setObject(value);
    }

    public void detach() {
        wrappedModel.detach();
    }
}
