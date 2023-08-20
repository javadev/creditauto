package org.bitbucket.creditauto.wicket;

import org.apache.wicket.RestartResponseException;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.LOGMarker;

public class LogoutPage extends TemplatePage {

    public LogoutPage() {
        getSession().invalidate();
        getSession().info("");
        LOG.info(this, "Logout succeeded");
        LOGMarker.instance().removeMarker();
        throw new RestartResponseException(HomePage.class);
    }
}
