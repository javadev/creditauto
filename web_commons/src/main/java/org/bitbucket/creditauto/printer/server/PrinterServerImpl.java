package org.bitbucket.creditauto.printer.server;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.wicket.protocol.http.WebApplication;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.printer.facade.IPrinter;
import org.bitbucket.creditauto.printer.facade.PrinterDocumentType;
import org.bitbucket.creditauto.printer.facade.PrinterException;

/**
 * PrinterServerImpl.
 */
public class PrinterServerImpl implements IPrinter {
    private In_instance inInstance;

    /**
     * Generates PDF from In_instance for the specified type.
     *
     * @param localInInstance In_instance object
     * @param documentType the document type
     * @throws PrinterException in case of printer error
     * @return the PDF binary image
     */
    public byte[] printDocumentFromInstance(
            In_instance localInInstance, PrinterDocumentType documentType) throws PrinterException {
        this.inInstance = localInInstance;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            createPDFJasper(Arrays.asList(documentType.getTemplateNames()), os);
            return os.toByteArray();
        } catch (Exception ex) {
            throw new PrinterException(ex.getMessage(), ex);
        }
    }

    /**
     * Reads instance from database and generates PDF for the specified type.
     *
     * @param dossierId dossier id
     * @param documentType the document type
     * @return the PDF binary image
     */
    public byte[] printDocumentFromDB(long dossierId, PrinterDocumentType documentType)
            throws PrinterException {
        return new byte[0];
    }

    /**
     * Reads PDF for specified file.
     *
     * @param fileNamePDF the PDF file name
     * @throws PrinterException in case of exception
     * @return the PDF binary image
     */
    public byte[] printSingleDocumentFromPDF(String fileNamePDF) throws PrinterException {
        return new byte[0];
    }

    private void createPDFJasper(List<String> templateNames, ByteArrayOutputStream os)
            throws PrinterException {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        InputStream fileIs = null;
        InputStream stringIs = null;
        try {
            String path = WebApplication.get().getServletContext().getRealPath("/WEB-INF");
            String decodedPath =
                    URLDecoder.decode(path, "UTF-8").replaceFirst("(.*?WEB-INF/classes)(.*)", "$1");
            try {
                for (String templateName : templateNames) {
                    fileIs =
                            new FileInputStream(decodedPath + "/templates/" + templateNames.get(0));
                    String contents =
                            applyVelocityTemplate(
                                    IOUtils.toString(fileIs, "UTF-8"), decodedPath + "/templates");
                    stringIs = IOUtils.toInputStream(contents, "UTF-8");
                    JasperReport jasperReport = JasperCompileManager.compileReport(stringIs);
                    jasperPrints.add(
                            JasperFillManager.fillReport(
                                    jasperReport, new HashMap(), new JREmptyDataSource()));
                }
                JasperPrint jasperPrint = jasperPrints.get(0);
                for (int index = 1; index < jasperPrints.size(); index += 1) {
                    List<JRPrintPage> pages = jasperPrints.get(index).getPages();
                    for (JRPrintPage page : pages) {
                        jasperPrint.addPage(page);
                    }
                }
                JasperExportManager.exportReportToPdfStream(jasperPrint, os);
            } finally {
                IOUtils.closeQuietly(fileIs);
                IOUtils.closeQuietly(stringIs);
            }
        } catch (Exception ex) {
            LOG.error(this, ex, ex.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private String applyVelocityTemplate(String templateData, String basePath) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty(
                "string.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        p.setProperty(
                "userdirective",
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
        repo.putStringResource(
                "template",
                templateData.replace(
                        "application-form-ukr.files", "${basePath}/application-form-ukr.files"));
        Template template = Velocity.getTemplate("template", "UTF-8");
        StringWriter writer = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("in_dossier", inInstance.getIn_dossier());
        context.put("in_person", inInstance.getIn_person());
        context.put(
                "in_person_fio",
                join(
                        inInstance.getIn_person().getLast_name(),
                        inInstance.getIn_person().getFirst_name(),
                        inInstance.getIn_person().getPatronymic_name()));
        context.put(
                "in_person_fio_ru",
                join(
                        inInstance.getIn_person().getLast_name_ru(),
                        inInstance.getIn_person().getFirst_name_ru(),
                        inInstance.getIn_person().getPatronymic_name_ru()));
        context.put("in_person_partner", inInstance.getIn_person_partner());
        context.put(
                "in_person_partner_fio",
                inInstance.getIn_person_partner() == null
                        ? ""
                        : join(
                                inInstance.getIn_person_partner().getLast_name(),
                                inInstance.getIn_person_partner().getFirst_name(),
                                inInstance.getIn_person_partner().getPatronymic_name()));
        context.put("in_guarantor", inInstance.getIn_guarantor());
        context.put(
                "in_guarantor_fio",
                inInstance.getIn_guarantor() == null
                        ? ""
                        : join(
                                inInstance.getIn_guarantor().getLast_name(),
                                inInstance.getIn_guarantor().getFirst_name(),
                                inInstance.getIn_guarantor().getPatronymic_name()));
        context.put("in_goods", inInstance.getIn_goods());
        context.put("basePath", basePath.replace("\\", "/"));
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
