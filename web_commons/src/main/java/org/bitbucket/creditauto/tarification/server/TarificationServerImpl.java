/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.tarification.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.Credittype;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.Product;
import org.bitbucket.creditauto.tarification.facade.CalculateOffersResult;
import org.bitbucket.creditauto.tarification.facade.CreditTypeInputParam;
import org.bitbucket.creditauto.tarification.facade.CreditTypesResult;
import org.bitbucket.creditauto.tarification.facade.ITarification;
import org.bitbucket.creditauto.tarification.facade.TarificationCalulateInputParam;
import org.bitbucket.creditauto.wicket.JpaRequestCycle;

/**
 * .
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class TarificationServerImpl implements ITarification {

    public CreditTypesResult getCreditTypes(CreditTypeInputParam param) {
        CreditTypesResult result = new CreditTypesResult();
        if (param.getTotalPrice() == null) {
            result.setErrorResult("Не найден не один кредитный тип для данного товара");
            return result;
        }
        if (param.getDownpayment() == null) {
            result.setErrorResult("Не найден не один кредитный тип для данного товара");
            return result;
        }
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query query = em.createQuery("select e from Externaldistributor e" + " where e.id = :id");
        query.setParameter("id", param.getExternaldistributor().getId());
        List<Externaldistributor> eds = query.getResultList();
        if (eds.get(0).getCredittypes() == null || eds.get(0).getCredittypes().isEmpty()) {
            result.setErrorResult("Не определен ни один кредитный тип для этого магазина");
            return result;
        }
        List<Credittype> credittypes = new ArrayList<Credittype>();
        BigDecimal amountOfLoan = param.getTotalPrice().subtract(param.getDownpayment());
        BigDecimal downpaymentPrc =
                param.getDownpayment()
                        .multiply(BigDecimal.valueOf(100D))
                        .divide(param.getTotalPrice(), 2, RoundingMode.HALF_UP);
        LOG.info(null, "downpaymentPrc - " + downpaymentPrc);
        for (Credittype ct : eds.get(0).getCredittypes()) {
            if (amountOfLoan.compareTo(ct.getTotalpricemin()) == -1) {
                continue;
            }
            if (amountOfLoan.compareTo(ct.getTotalpricemax()) == 1) {
                continue;
            }
            if (downpaymentPrc.compareTo(ct.getDownpaymentmin()) == -1) {
                continue;
            }

            credittypes.add(ct);
        }
        if (credittypes.isEmpty()) {
            result.setErrorResult("Не найден не один кредитный тип для данного товара");
            return result;
        }
        result.credittypes = credittypes;
        return result;
    }

    public List<Product> getProductTypes() {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query query = em.createQuery("select p from Product p");
        List<Product> products = query.getResultList();
        return products;
    }

    public CalculateOffersResult calculateOffers(TarificationCalulateInputParam param) {
        return new CalculateOffersResult();
    }
}
