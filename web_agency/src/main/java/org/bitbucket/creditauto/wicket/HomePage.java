/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.authorization.facade.AuthorizationResult;
import org.bitbucket.creditauto.authorization.facade.IAuthorization;
import org.bitbucket.creditauto.authorization.server.AuthorizationServerImpl;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class HomePage extends WebPage {

    public HomePage() {
        add(new LoginForm("form"));
        setStatelessHint(true);
    }

    private static class LoginForm extends StatelessForm {

        private TextField username = new TextField("username", new Model(""));
        private TextField password = new PasswordTextField("password", new Model(""));
        private TextField shopId = new TextField("shopId", new Model(""));
        private IAuthorization authorization;

        public LoginForm(String id) {
            super(id);
            add(username);
            add(password.setRequired(false));
            add(shopId.setRequired(true));
            add(new FeedbackPanel("messages"));
            authorization = new AuthorizationServerImpl();
        }

        @Override
        protected void onSubmit() {
            AuthorizationResult result =
                    authorization.login(
                            username.getInput(), password.getInput(), shopId.getInput(), "");
            if (result.isError) {
                error(result.messageKey);
                return;
            }
            CreditautoSession session = CreditautoSession.get();
            session.setUser(result.user);
            session.setUserShop(result.userShop);
            session.bind();
            LOG.info(this, "Login succeeded");
            session.info("Добро пожаловать, " + result.user.getName());
            setResponsePage(MainPage.class);
        }
    }
}
