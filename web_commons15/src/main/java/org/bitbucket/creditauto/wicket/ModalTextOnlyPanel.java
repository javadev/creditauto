/*
 * $Id$
 *
 * Copyright (c) 2011 (alisa)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**.
 * @author alisa
 * @version $Revision$ $Date$
 */
public class ModalTextOnlyPanel extends Panel {


    public ModalTextOnlyPanel(String id, String labelText, boolean isConfirmationPanel) {
        super(id);

        add(new Label("text", labelText));
        add((new AjaxLink("yes") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onConfirm(target);
            }
        }).setVisible(isConfirmationPanel));

        add((new AjaxLink("no") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancel(target);
            }
        }).setVisible(isConfirmationPanel));
    }

    protected  void onConfirm(AjaxRequestTarget target) {
    }
    protected  void onCancel(AjaxRequestTarget target) {
    }
}
