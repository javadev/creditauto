/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.bitbucket.creditauto.wicket.workflow.GoodSelectionPage;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({"unchecked", "serial"})
public class CreditautoApplication extends CommonApplication {

    private final class CreditautoAuthorizationStrategy implements IAuthorizationStrategy {
        public boolean isActionAuthorized(Component c, Action a) {
            return true;
        }

        public boolean isInstantiationAuthorized(Class clazz) {
            if (TemplatePage.class.isAssignableFrom(clazz)) {
                if (CreditautoSession.get().getUser() == null) {
                    throw new RestartResponseException(HomePage.class);
                }
            }
            return true;
        }
    }

    @Override
    public Class getHomePage() {
        return MainPage.class;
    }

    @Override
    public void init() {
        super.init();
        getSecuritySettings().setAuthorizationStrategy(new CreditautoAuthorizationStrategy());
        mountBookmarkablePage("/home", HomePage.class);
        mountBookmarkablePage("/logout", LogoutPage.class);
        mountBookmarkablePage("/changepassword", PasswordPage.class);
        mountBookmarkablePage("/productselection", GoodSelectionPage.class);
    }
}
