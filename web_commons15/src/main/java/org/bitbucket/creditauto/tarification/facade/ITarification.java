/*
 * $Id$
 */
package org.bitbucket.creditauto.tarification.facade;

import java.util.List;

import org.bitbucket.creditauto.entity.Product;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public interface ITarification {

    CreditTypesResult getCreditTypes(CreditTypeInputParam param);
    List<Product> getProductTypes();
}
