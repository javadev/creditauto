/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.printer.facade;

import org.bitbucket.creditauto.entity.In_instance;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public interface IPrinter {
    /**
     * Generates PDF from In_instance for the specified type.
     * @param instance In_instance object
     * @param documentType the document type
     * @return the PDF binary image
     */
    byte [] printDocumentFromInstance(In_instance instance, PrinterDocumentType documentType) throws PrinterException;

    /**
     * Reads instance from database and generates PDF for the specified type.
     * @param dossierId dossier id
     * @param documentType the document type
     * @return the PDF binary image
     */
    byte [] printDocumentFromDB(long dossierId, PrinterDocumentType documentType) throws PrinterException;

    /**
     * Reads PDF for specified file.
     * @param fileNamePDF the PDF file name
     * @return the PDF binary image
     */
    byte [] printSingleDocumentFromPDF(String fileNamePDF) throws PrinterException;

}
