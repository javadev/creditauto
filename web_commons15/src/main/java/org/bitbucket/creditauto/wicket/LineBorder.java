/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class LineBorder extends Border {

    public LineBorder(String id, WebMarkupContainer wmc) {
        super(id);
        add(wmc);
    }
}