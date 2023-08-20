/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.tarification.facade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.In_good;
import org.bitbucket.creditauto.entity.User;

/**
 * .
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class CreditTypeInputParam implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<In_good> goods;
    private final Externaldistributor externaldistributor;
    private final BigDecimal downpayment;
    private final BigDecimal totalPrice;
    private final User user;

    public CreditTypeInputParam(
            List<In_good> goods,
            Externaldistributor externaldistributor,
            BigDecimal downpayment,
            BigDecimal totalPrice,
            User user) {
        this.goods = goods;
        this.externaldistributor = externaldistributor;
        this.downpayment = downpayment;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public List<In_good> getGoods() {
        return goods;
    }

    public Externaldistributor getExternaldistributor() {
        return externaldistributor;
    }

    public BigDecimal getDownpayment() {
        return downpayment;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public User getUser() {
        return user;
    }
}
