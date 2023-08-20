/*
 * $Id$
 */
package org.bitbucket.creditauto.printer.server;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.printer.facade.IPrinter;
import org.bitbucket.creditauto.printer.facade.PrinterDocumentType;
import org.bitbucket.creditauto.printer.facade.PrinterException;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import org.apache.wicket.protocol.http.WebApplication;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * PrinterServerImpl.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class PrinterServerImpl implements IPrinter {
    private In_instance inInstance;

    /**
     * Generates PDF from In_instance for the specified type.
     * @param localInInstance In_instance object
     * @param documentType the document type
     * @throws PrinterException in case of printer error
     * @return the PDF binary image
     */
    public byte[] printDocumentFromInstance(In_instance localInInstance,
            PrinterDocumentType documentType) throws PrinterException {
        this.inInstance = localInInstance;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            createPDF(documentType.getTemplateName(), os);
            return os.toByteArray();
        } catch (Exception ex) {
            throw new PrinterException(ex.getMessage(), ex);
        }
    }

    /**
     * Reads instance from database and generates PDF for the specified type.
     * @param dossierId dossier id
     * @param documentType the document type
     * @return the PDF binary image
     */
    public byte[] printDocumentFromDB(long dossierId, PrinterDocumentType documentType) throws PrinterException {
        return new byte[0];
    }


    /**
     * Reads PDF for specified file.
     * @param fileNamePDF the PDF file name
     * @throws PrinterException in case of exception
     * @return the PDF binary image
     */
    public byte[] printSingleDocumentFromPDF(String fileNamePDF) throws PrinterException {
        return new byte[0];
    }

    private void createPDF(String templateName, ByteArrayOutputStream os)
            throws IOException, DocumentException {
        InputStream fileIs = null;
        InputStream stringIs = null;
        try {
//            String path = PrinterServerImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String path = WebApplication.get().getServletContext().getRealPath("/WEB-INF") ;
            String decodedPath = URLDecoder.decode(path, "UTF-8").replaceFirst("(.*?WEB-INF/classes)(.*)", "$1");
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(decodedPath + "/fonts/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(decodedPath + "/fonts/arialbd.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(decodedPath + "/fonts/arialbi.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.getFontResolver().addFont(decodedPath + "/fonts/ariali.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            fileIs = new FileInputStream(decodedPath + "/templates/" + templateName);
            String contents = applyVelocityTemplate(IOUtils.toString(fileIs, "UTF-8"));
            stringIs = IOUtils.toInputStream(contents, "UTF-8");
            Document doc = XMLResource.load(stringIs).getDocument();
            renderer.setDocument(doc, "file:///" + decodedPath + "/templates/" + templateName);
            renderer.layout();
            renderer.createPDF(os);

            LOG.info(this, "Document was created, size=" + os.toByteArray().length);
        } catch (Exception ex) {
            LOG.error(this, ex, ex.getMessage());
        } finally {
            IOUtils.closeQuietly(fileIs);
            IOUtils.closeQuietly(stringIs);
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
    }

    private String applyVelocityTemplate(String templateData) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty("resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        p.setProperty("userdirective",
                "org.bitbucket.creditauto.printer.server.MoneyDirective,"
                + "org.bitbucket.creditauto.printer.server.MoneyUAHDirective,"
                + "org.bitbucket.creditauto.printer.server.MoneyToStrDirective,"
                + "org.bitbucket.creditauto.printer.server.DateDirective,"
                + "org.bitbucket.creditauto.printer.server.DictionaryDirective,"
                + "org.bitbucket.creditauto.printer.server.DictionaryExpDirective,"
                + "org.bitbucket.creditauto.printer.server.DictionaryExp2Directive,"
                + "org.bitbucket.creditauto.printer.server.JoinDirective");
        Velocity.init(p);

        StringResourceRepository repo = StringResourceLoader.getRepository();
        repo.putStringResource("template", templateData);
        Template template = Velocity.getTemplate("template", "UTF-8");
        StringWriter writer = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("in_dossier", inInstance.getIn_dossier());
        context.put("in_person", inInstance.getIn_person());
        context.put("in_person_fio", join(inInstance.getIn_person().getLast_name(),
            inInstance.getIn_person().getFirst_name(), inInstance.getIn_person().getPatronymic_name()));
        context.put("in_person_fio_ru", join(inInstance.getIn_person().getLast_name_ru(),
            inInstance.getIn_person().getFirst_name_ru(), inInstance.getIn_person().getPatronymic_name_ru()));
        context.put("in_person_partner", inInstance.getIn_person_partner());
        context.put("in_person_partner_fio", inInstance.getIn_person_partner() == null ? "" : join(inInstance.getIn_person_partner().getLast_name(),
            inInstance.getIn_person_partner().getFirst_name(), inInstance.getIn_person_partner().getPatronymic_name()));
        context.put("in_guarantor", inInstance.getIn_guarantor());
        context.put("in_guarantor_fio", inInstance.getIn_guarantor() == null ? "" : join(inInstance.getIn_guarantor().getLast_name(),
            inInstance.getIn_guarantor().getFirst_name(), inInstance.getIn_guarantor().getPatronymic_name()));
        context.put("in_goods", inInstance.getIn_goods());
        template.merge(context, writer);
        writer.flush();
        writer.close();
        return writer.toString();
    }

    public static String join(String... strings) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (String string : strings) {
            if (string != null) {
                result.append(string);
            }
            if (index < strings.length) {
                result.append(" ");
            }
            index += 1;
        }
        return result.toString().trim().replaceAll("\\s+", " ");
    }
}
