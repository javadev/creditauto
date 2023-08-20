/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class MainPage extends TemplatePage {

    public MainPage() {
        add(new FeedbackPanel("messages"));
    }

    /** Fix firefox issue. http://issues.apache.org/jira/browse/WICKET-923 */
    @Override
    protected void configureResponse() {
        super.configureResponse();

        final WebResponse response = getWebRequestCycle().getWebResponse();
        response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store");
    }
}
