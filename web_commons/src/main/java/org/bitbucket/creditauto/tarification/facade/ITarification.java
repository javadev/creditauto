package org.bitbucket.creditauto.tarification.facade;

import java.util.List;
import org.bitbucket.creditauto.entity.Product;

/**
 * .
 */
public interface ITarification {

    CreditTypesResult getCreditTypes(CreditTypeInputParam param);

    List<Product> getProductTypes();
}
