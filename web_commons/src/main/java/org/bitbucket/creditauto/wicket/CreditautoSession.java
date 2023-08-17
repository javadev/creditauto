/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.entity.User;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({ "serial" })
public class CreditautoSession extends WebSession {

    private User user;
    private Externaldistributor userShop;
    private In_instance inInstance = new In_instance(true);
    private SearchData searchData = new SearchData();
    private int pageSize = 10;
    private int page;

    public static CreditautoSession get() {
        return (CreditautoSession) WebSession.get();
    }

    public CreditautoSession(Request request) {
        super(request);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Externaldistributor getUserShop() {
        return userShop;
    }

    public void setUserShop(Externaldistributor userShop) {
        this.userShop = userShop;
    }

    public In_instance getInInstance() {
        return inInstance;
    }

    public void setInInstance(In_instance inInstance) {
        this.inInstance = inInstance;
    }

    public SearchData getSearchData() {
        return searchData;
    }

    public void setSearchData(SearchData searchData) {
        this.searchData = searchData;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
