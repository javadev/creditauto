/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.wicket.Component;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressingWebRequestProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.convert.ConverterLocator;
import org.apache.wicket.util.convert.converters.BigDecimalConverter;
import org.apache.wicket.util.convert.converters.DateConverter;
import org.bitbucket.creditauto.wicket.workflow.GoodSelectionPage;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({ "unchecked", "serial" })
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
