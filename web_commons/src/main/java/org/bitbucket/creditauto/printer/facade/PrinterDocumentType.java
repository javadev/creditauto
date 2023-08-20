package org.bitbucket.creditauto.printer.facade;

public enum PrinterDocumentType {
    DEFAULT,
    SIMULATION,
    APPLICATION_FORM("application-form-ukr.jrxml", "application-form-ukr-page2.jrxml"),
    CONTRACT,
    DAILY_REPORT_CAR,
    GOODS_RETURN,
    AGENCY_REPORT_CAR;

    private String[] templateNames;

    private PrinterDocumentType() {}

    private PrinterDocumentType(String... templateNames) {
        this.templateNames = templateNames;
    }

    public String[] getTemplateNames() {
        return templateNames;
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
