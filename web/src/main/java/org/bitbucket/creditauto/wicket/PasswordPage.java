/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.bitbucket.creditauto.authorization.facade.AuthorizationResult;
import org.bitbucket.creditauto.authorization.facade.IAuthorization;
import org.bitbucket.creditauto.authorization.server.AuthorizationServerImpl;
import org.bitbucket.creditauto.entity.User;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({ "unchecked", "serial" })
public class PasswordPage extends TemplatePage {

    public PasswordPage() {
        add(new PasswordForm("form"));
    }

    private class PasswordForm extends Form {

        private class UserData {
            private String oldpassword;
            private String password;
            private String verify;
        }
        private User user = getCreditautoSession().getUser();
        private UserData userData = new UserData();
        private IAuthorization authorization;
        private FormComponent passwordField;

        public PasswordForm(String id) {
            super(id);
            setModel(new CompoundPropertyModel(userData));
            FormComponent oldPasswordField = new PasswordTextField("oldpassword");
            add(new EditBorder("oldPasswordBorder", oldPasswordField));
            passwordField = new PasswordTextField("password");
            add(new EditBorder("passwordBorder", passwordField));
            FormComponent verifyField = new PasswordTextField("verify", new Model(""));
            add(new EditBorder("verifyBorder", verifyField));
            add(new EqualPasswordInputValidator(passwordField, verifyField));
            add(new BookmarkablePageLink("cancel", MainPage.class));
            authorization = new AuthorizationServerImpl();
        }

        @Override
        protected void onSubmit() {
            AuthorizationResult result = authorization.changePassword(user, userData.oldpassword, userData.password);
            if (result.isError) {
                passwordField.error(result.messageKey);
                return;
            }
            getSession().info(getString("passwordWasChanged"));
            setResponsePage(MainPage.class);
        }
    }
}
