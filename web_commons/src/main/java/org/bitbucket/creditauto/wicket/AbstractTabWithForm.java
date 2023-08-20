/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public abstract class AbstractTabWithForm extends AbstractTab {
    private static final long serialVersionUID = 1L;

    public AbstractTabWithForm(IModel title) {
        super(title);
    }

    // override this if you have a form
    public Form getForm() {
        return null;
    }
}
