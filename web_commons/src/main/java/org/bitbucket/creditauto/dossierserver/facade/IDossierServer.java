/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.dossierserver.facade;

import java.util.Date;
import java.util.List;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.Product;
import org.bitbucket.creditauto.entity.User;

public interface IDossierServer {
    DossierResult initDossier(Externaldistributor externaldistributor, User user, Product product);
}
