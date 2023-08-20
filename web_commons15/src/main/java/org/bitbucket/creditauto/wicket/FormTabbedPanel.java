package org.bitbucket.creditauto.wicket;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

public class FormTabbedPanel extends AjaxTabbedPanel {
    private static final long serialVersionUID = 1L;
 
    public FormTabbedPanel(final String id, final List<ITab> tabs) {
        super(id, tabs);
    }

    @Override
    protected WebMarkupContainer newLink(String linkId, final int index) {
        Form form = ((AbstractTabWithForm) getTabs().get(getSelectedTab())).getForm();
 
        if (form == null || getSelectedTab() == getTabs().size() - 1) {
            return super.newLink(linkId, index);
        } else {
            return new AjaxSubmitLink(linkId, form) {
                private static final long serialVersionUID = 1L;
 
                @Override
                protected void onError(AjaxRequestTarget target, Form form) {
//                    super.onError(target, form);
                    target.addComponent(FormTabbedPanel.this);
                }
 
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    setSelectedTab(index);
                    if (target != null) {
                        target.addComponent(FormTabbedPanel.this);
                    }
                }
            };
        }
    }
}
