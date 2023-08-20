/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.printer.facade;

import java.io.Serializable;

/**
 * .
 *
 * @author vko
 * @version $Revision$ $Date$
 */
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
