package org.bitbucket.creditauto.wicket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;

public class JpaRequestCycle extends AbstractRequestCycleListener {

    private EntityManager em;
    private WebApplication application;
    private static JpaRequestCycle cycle;

    public static JpaRequestCycle get() {
        return cycle;
    }

    public JpaRequestCycle(WebApplication application) {
        this.application = application;
        this.cycle = this;
    }

    public EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory emf = ((CommonApplication) getApplication()).getEntityManagerFactory();
            em = emf.createEntityManager();
            em.getTransaction().begin();
        }
        return em;
    }

    /**
     * Pushes an NDC with the unique session ID
     */
    public void onBeginRequest() {
        org.bitbucket.creditauto.LOGMarker.instance().setUserAndInstanceId(
            CreditautoSession.get().getUser() == null ? "" : CreditautoSession.get().getUser().getLogin(),
            CreditautoSession.get().getInInstance() == null || CreditautoSession.get().getInInstance().getIn_dossier().getId() == null ? ""
                : String.valueOf(CreditautoSession.get().getInInstance().getIn_dossier().getId()));
    }

    public void onEndRequest() {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
    }

    public WebApplication getApplication() {
        return application;
    }

/*
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
*/

}
