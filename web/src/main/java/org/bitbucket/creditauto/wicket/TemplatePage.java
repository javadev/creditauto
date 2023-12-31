package org.bitbucket.creditauto.wicket;

import javax.persistence.EntityManager;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.bitbucket.creditauto.authorization.server.AuthorizationServerImpl;
import org.bitbucket.creditauto.entity.In_instance;

@SuppressWarnings({"unchecked"})
public class TemplatePage extends WebPage {

    public TemplatePage() {
        add(new Label("userName", new PropertyModel(this, "session.user.name")));
        add(new Label("shopName", new PropertyModel(this, "session.userShop.name")));
        add(new BookmarkablePageLink("logout", LogoutPage.class));
        add(
                new BookmarkablePageLink(
                        "goodSelectionPageMenu",
                        org.bitbucket.creditauto.wicket.workflow.GoodSelectionPage.class));
        add(
                new BookmarkablePageLink(
                        "goodSelectionPage",
                        org.bitbucket.creditauto.wicket.workflow.GoodSelectionPage.class));
        add(
                new BookmarkablePageLink(
                        "requestsListPageMenu",
                        org.bitbucket.creditauto.wicket.workflow.RequestsListPage.class));
        add(
                new BookmarkablePageLink(
                        "requestsListPage",
                        org.bitbucket.creditauto.wicket.workflow.RequestsListPage.class));
        add(
                new BookmarkablePageLink(
                        "usersListPage",
                        org.bitbucket.creditauto.wicket.workflow.UsersListPage.class));
        add(
                new BookmarkablePageLink(
                        "addNewUserPage",
                        org.bitbucket.creditauto.wicket.workflow.AddNewUserPage.class));
        add(new BookmarkablePageLink("passwordPage", PasswordPage.class));
        add(
                new BookmarkablePageLink(
                        "complainPage",
                        org.bitbucket.creditauto.wicket.workflow.ComplainPage.class));
        add(new Label("title", "Personal finance::Главная страница"));
        String calledMe = whoCalledMe();
        if (!calledMe.contains("PasswordPage")
                && !calledMe.contains("LogoutPage")
                && !new AuthorizationServerImpl()
                        .isPasswordValid(getCreditautoSession().getUser())) {
            throw new RestartResponseException(PasswordPage.class);
        }
    }

    protected EntityManager getEntityManager() {
        return ((JpaRequestCycle) getRequestCycle()).getEntityManager();
    }

    protected void endConversation() {
        ((JpaRequestCycle) getRequestCycle()).endConversation();
    }

    protected CreditautoSession getCreditautoSession() {
        return (CreditautoSession) getSession();
    }

    protected void saveWorkflowPath(String uiName) {
        final In_instance inInstance = getCreditautoSession().getInInstance();
        inInstance.getIn_dossier().setUi_state(uiName);
        if (inInstance.getIn_dossier().getUi_workflow_path() == null) {
            inInstance.getIn_dossier().setUi_workflow_path(uiName);
        } else if (!inInstance.getIn_dossier().getUi_workflow_path().contains(uiName)) {
            inInstance
                    .getIn_dossier()
                    .setUi_workflow_path(
                            inInstance.getIn_dossier().getUi_workflow_path() + "|" + uiName);
        }
    }

    private String whoCalledMe() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTraceElements[3];
        String classname = caller.getClassName();
        String methodName = caller.getMethodName();
        int lineNumber = caller.getLineNumber();
        return classname + "." + methodName + ":" + lineNumber;
    }
}
