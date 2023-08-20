/*
 * $Id$
 */
package org.bitbucket.creditauto.dossierserver.server;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.dossierserver.facade.DossierResult;
import org.bitbucket.creditauto.dossierserver.facade.IDossierServer;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.entity.Product;
import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.wicket.JpaRequestCycle;

/**
 * DossierServerServerImpl.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class DossierServerServerImpl implements IDossierServer {

    public DossierResult initDossier(
            Externaldistributor externaldistributor, User user, Product product) {
        DossierResult result = new DossierResult();
        if (externaldistributor == null) {
            throw new IllegalArgumentException("externaldistributor is null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (product == null) {
            throw new IllegalArgumentException("product is null");
        }
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query query = em.createQuery("select p from Product p" + " where p.id = :id");
        query.setParameter("id", product.getId());
        List<Product> products = query.getResultList();
        if (products.isEmpty()) {
            throw new IllegalArgumentException("products is empty");
        }
        In_instance inInstance = new In_instance(true);
        inInstance.getIn_dossier().setExternaldistributor(externaldistributor);
        inInstance.getIn_dossier().setProduct(products.get(0));
        inInstance.getIn_dossier().setDate_of_entering_dossier(new Date());
        inInstance.getIn_dossier().setUser_name_enters_dossier(user.getName());
        result.inInstance = inInstance;
        LOG.info(this, "inInstance - " + inInstance);
        return result;
    }
}
