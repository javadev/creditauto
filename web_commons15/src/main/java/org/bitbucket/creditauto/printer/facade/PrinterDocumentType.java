/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.printer.facade;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public enum PrinterDocumentType {
    DEFAULT,
    SIMULATION,
    APPLICATION_FORM("application-form-ukr-template.html"),
    CONTRACT,
    DAILY_REPORT_CAR,
    GOODS_RETURN,
    AGENCY_REPORT_CAR;

    private String templateName;

    private PrinterDocumentType() {
    }

    private PrinterDocumentType(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public static PrinterDocumentType find(final String name) {
        for (final PrinterDocumentType item : values()) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return DEFAULT;
    }
}
