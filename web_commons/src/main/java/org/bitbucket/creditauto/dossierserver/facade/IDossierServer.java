/*
 * $Id$
 */
package org.bitbucket.creditauto.dossierserver.facade;

import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.Product;
import org.bitbucket.creditauto.entity.User;

public interface IDossierServer {
    DossierResult initDossier(Externaldistributor externaldistributor, User user, Product product);
}
