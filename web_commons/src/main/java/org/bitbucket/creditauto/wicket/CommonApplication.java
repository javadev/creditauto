/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressingWebRequestProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.convert.ConverterLocator;
import org.apache.wicket.util.convert.converters.BigDecimalConverter;
import org.apache.wicket.util.convert.converters.DateConverter;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({"unchecked", "serial"})
public class CommonApplication extends WebApplication {
    private NonThreadedModificationWatcher resourceWatcher = new NonThreadedModificationWatcher();

    private EntityManagerFactory emf;

    @Override
    public Class getHomePage() {
        return WebPage.class;
    }

    @Override
    public void init() {
        // Enable non threaded modification watching (required for GAE)
        getResourceSettings().setResourceWatcher(resourceWatcher);
        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        super.init();
        emf = Persistence.createEntityManagerFactory("creditautoDatabase");
        getMarkupSettings().setCompressWhitespace(true);
    }

    @Override
    public RequestCycle newRequestCycle(Request request, Response response) {
        return new JpaRequestCycle(this, (WebRequest) request, response);
    }

    @Override
    public CreditautoSession newSession(Request request, Response response) {
        return new CreditautoSession(request);
    }

    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new UrlCompressingWebRequestProcessor();
    }

    @Override
    protected ISessionStore newSessionStore() {
        return new HttpSessionStore(this);
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator converterLocator = new ConverterLocator();
        BigDecimalConverter converter =
                new BigDecimalConverter() {
                    @Override
                    public NumberFormat getNumberFormat(Locale locale) {
                        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
                        DecimalFormat df = (DecimalFormat) nf;
                        df.applyPattern(",##0.00");
                        return nf;
                    }
                };
        DateConverter dateConverter =
                new DateConverter() {
                    @Override
                    public DateFormat getDateFormat(Locale locale) {
                        return new SimpleDateFormat("ddMMyyyy");
                    }
                };
        converterLocator.set(BigDecimal.class, converter);
        converterLocator.set(Date.class, dateConverter);
        converterLocator.set(java.sql.Timestamp.class, dateConverter);
        return converterLocator;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
