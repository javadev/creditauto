package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

public class LineBorder extends Border {

    public LineBorder(String id, WebMarkupContainer wmc) {
        super(id);
        add(wmc);
    }
}
