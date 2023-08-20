/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class JpaRequestCycle extends WebRequestCycle {

    private EntityManager em;
    private boolean endConversation;

    public static JpaRequestCycle get() {
        return (JpaRequestCycle) RequestCycle.get();
    }

    public JpaRequestCycle(WebApplication application, WebRequest request, Response response) {
        super(application, request, response);
    }

    public EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory emf =
                    ((CommonApplication) getApplication()).getEntityManagerFactory();
            em = emf.createEntityManager();
            em.getTransaction().begin();
        }
        return em;
    }

    /** Pushes an NDC with the unique session ID */
    @Override
    protected void onBeginRequest() {
        super.onBeginRequest();
        org.bitbucket.creditauto.LOGMarker.instance()
                .setUserAndInstanceId(
                        CreditautoSession.get().getUser() == null
                                ? ""
                                : CreditautoSession.get().getUser().getLogin(),
                        CreditautoSession.get().getInInstance() == null
                                        || CreditautoSession.get()
                                                        .getInInstance()
                                                        .getIn_dossier()
                                                        .getId()
                                                == null
                                ? ""
                                : String.valueOf(
                                        CreditautoSession.get()
                                                .getInInstance()
                                                .getIn_dossier()
                                                .getId()));
    }

    @Override
    protected void onEndRequest() {
        super.onEndRequest();
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
        if (endConversation) {
            getRequest().getPage().getPageMap().remove();
        }
    }

    @Override
    public Page onRuntimeException(Page page, RuntimeException e) {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
        if (e instanceof PageExpiredException) {
            getSession().error("The page you requested has expired.");
            return page;
        }
        return super.onRuntimeException(page, e);
    }

    public void endConversation() {
        endConversation = true;
    }
}
