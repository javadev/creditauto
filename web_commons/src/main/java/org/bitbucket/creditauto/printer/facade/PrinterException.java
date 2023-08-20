package org.bitbucket.creditauto.printer.facade;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PrinterException extends Exception implements Serializable {

    public PrinterException() {
        super();
    }

    public PrinterException(String msg) {
        super(msg);
    }

    public PrinterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
