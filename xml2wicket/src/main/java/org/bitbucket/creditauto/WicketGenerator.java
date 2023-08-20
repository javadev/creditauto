package org.bitbucket.creditauto;

import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

/**
 * WicketGenerator.
 */
public class WicketGenerator {

    private String basedir;
    private List<String> files;
    private String outPackage;
    private List<Map<String, String>> variables;
    private List<Map<String, String>> buttons;
    private List<Repeater> repeaters;
    /** Instance logger */
    private Log log;

    /** . */
    public class Repeater {
        private List<Map<String, String>> headers = new ArrayList<Map<String, String>>();
        private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        private List<Map<String, String>> links = new ArrayList<Map<String, String>>();
        private Map<String, String> extra = new HashMap<String, String>();

        public List<Map<String, String>> getHeaders() {
            return this.headers;
        }

        public void setHeaders(List<Map<String, String>> headers) {
            this.headers = headers;
        }

        public List<Map<String, String>> getData() {
            return this.data;
        }

        public void setData(List<Map<String, String>> data) {
            this.data = data;
        }

        public List<Map<String, String>> getLinks() {
            return this.links;
        }

        public void setLinks(List<Map<String, String>> links) {
            this.links = links;
        }

        public Map<String, String> getExtra() {
            return this.extra;
        }

        public void setExtra(Map<String, String> extra) {
            this.extra = extra;
        }
    }

    public WicketGenerator(String basedir, List<String> files, String outPackage) {
        this.basedir = basedir;
        this.files = files;
        this.outPackage = outPackage;
    }

    /**
     * By default, return a <code>SystemStreamLog</code> logger.
     *
     * @see org.apache.maven.plugin.Mojo#getLog()
     */
    public Log getLog() {
        if (log == null) {
            log = new SystemStreamLog();
        }
        return log;
    }

    public void generate() {
        generateJava();
    }

    private void generateJava() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty(
                "resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        Velocity.init(p);

        Template template = getTemplate("Wicket.java");
        Template templatePageHtml = getTemplate("WicketPage.html");
        Template templateHtml;
        for (String file : files) {
            XMLTag tag = getXmlTag(file);
            int classCount = tag.rawXpathNodeSet("class").getLength();
            for (int index = 1; index <= classCount; index += 1) {
                getVariables(tag, "class[" + index + "]");
                getButtons(tag, "class[" + index + "]");
                getRepeaters(tag, "class[" + index + "]");
                String classname = tag.rawXpathString("class[" + index + "]/@name");
                String extend = tag.rawXpathString("class[" + index + "]/@extends");

                String templateAttr = tag.rawXpathString("class[" + index + "]/@template");
                templateHtml = getTemplate(templateAttr.isEmpty() ? "Wicket.html" : templateAttr);
                String formInit = tag.rawXpathString("class[" + index + "]/formInit");
                String formMethods = tag.rawXpathString("class[" + index + "]/formMethods");
                String pageInit = tag.rawXpathString("class[" + index + "]/pageInit");
                String pageMethods = tag.rawXpathString("class[" + index + "]/pageMethods");
                String compoundPropertyModel =
                        tag.rawXpathString("class[" + index + "]/compoundPropertyModel");

                VelocityContext context = new VelocityContext();
                context.put("classname", classname);
                context.put("extends", extend);
                context.put("package", outPackage);
                context.put("variables", variables);
                context.put("buttons", buttons);
                context.put("repeaters", repeaters);

                context.put("formInit", formInit);
                context.put("formMethods", formMethods);

                context.put("pageInit", pageInit);
                context.put("pageMethods", pageMethods);
                context.put("compoundPropertyModel", compoundPropertyModel);

                context.put("xmltag", tag.duplicate().gotoTag("class[" + index + "]"));
                try {
                    String outDirs =
                            basedir + "/src/main/java/" + outPackage.replaceAll("\\.", "/");
                    new File(outDirs).mkdirs();
                    Writer writer =
                            new OutputStreamWriter(
                                    new FileOutputStream(outDirs + "/" + classname + ".java"),
                                    "utf-8");
                    template.merge(context, writer);
                    writer.flush();
                    writer.close();
                    writer =
                            new OutputStreamWriter(
                                    new FileOutputStream(outDirs + "/" + classname + ".html"),
                                    "utf-8");
                    if (classname.endsWith("Panel")) {
                        templateHtml.merge(context, writer);
                    } else {
                        templatePageHtml.merge(context, writer);
                    }
                    writer.flush();
                    writer.close();
                    if (!classname.endsWith("Panel")) {
                        writer =
                                new OutputStreamWriter(
                                        new FileOutputStream(
                                                outDirs + "/" + classname + "$MainPanel.html"),
                                        "utf-8");
                        templateHtml.merge(context, writer);
                        writer.flush();
                        writer.close();
                    }
                } catch (IOException e) {
                    getLog().error(e);
                }
            }
        }
    }

    /**
     * Get a template from the cache; if template has not been loaded, load it
     *
     * @param templatePath
     * @return
     */
    private Template getTemplate(final String templatePath) {
        if (!Velocity.resourceExists(templatePath)) {
            StringResourceRepository repo = StringResourceLoader.getRepository();
            repo.putStringResource(templatePath, getTemplateFromResource(templatePath));
        }
        return Velocity.getTemplate(templatePath);
    }

    /**
     * Read a template into memory
     *
     * @param templatePath
     * @return
     */
    private String getTemplateFromResource(final String templatePath) {
        try {
            InputStream stream =
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(templatePath);
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private XMLTag getXmlTag(final String xmlFileName) {
        return XMLDoc.from(new File(xmlFileName), true);
    }

    private void getRepeaters(XMLTag xmlTag, String className) {
        repeaters = new ArrayList<Repeater>();
        List<Map<String, String>> headers = new ArrayList<Map<String, String>>();
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        List<Map<String, String>> links = new ArrayList<Map<String, String>>();
        Map<String, String> extra = new HashMap<String, String>();

        int index = 1;
        while (true) {
            String name = xmlTag.rawXpathString(className + "/repeater[" + index + "]/@name");
            String type = xmlTag.rawXpathString(className + "/repeater[" + index + "]/@type");
            // String size = xmlTag.rawXpathString(className + "/repeater["+index+"]/@size");
            String model = xmlTag.rawXpathString(className + "/repeater[" + index + "]/model");
            String modelClass =
                    xmlTag.rawXpathString(className + "/repeater[" + index + "]/modelClass");

            if (name.isEmpty()) {
                break;
            }

            extra.put("name", name);
            extra.put("type", type);
            extra.put("model", model);
            extra.put("modelClass", modelClass);

            int columns = 1;
            while (true) {
                String hname =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/header["
                                        + columns
                                        + "]/@name");
                String hlabel =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/header["
                                        + columns
                                        + "]/@label");
                String hsize =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/header["
                                        + columns
                                        + "]/@size");

                String dname =
                        xmlTag.rawXpathString(
                                className + "/repeater[" + index + "]/data[" + columns + "]/@name");
                String dlabel =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/data["
                                        + columns
                                        + "]/@label");
                String dsize =
                        xmlTag.rawXpathString(
                                className + "/repeater[" + index + "]/data[" + columns + "]/@size");
                String dtype =
                        xmlTag.rawXpathString(
                                className + "/repeater[" + index + "]/data[" + columns + "]/@type");
                String dcssclass =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/data["
                                        + columns
                                        + "]/@cssclass");
                String donClick =
                        xmlTag.rawXpathString(
                                className
                                        + "/repeater["
                                        + index
                                        + "]/data["
                                        + columns
                                        + "]/onClick");
                String dmodel =
                        xmlTag.rawXpathString(
                                className + "/repeater[" + index + "]/data[" + columns + "]/model");
                if ("links".equals(dtype) && links.isEmpty()) {
                    links =
                            getLinksList(
                                    xmlTag,
                                    className
                                            + "/repeater["
                                            + index
                                            + "]/data["
                                            + columns
                                            + "]/links");
                }

                if (hname.isEmpty()) {
                    break;
                }
                Map<String, String> header = new HashMap<String, String>();
                header.put("name", hname);
                header.put("label", hlabel);
                header.put("size", hsize);
                headers.add(header);

                Map<String, String> data = new HashMap<String, String>();
                data.put("name", dname);
                data.put("label", dlabel);
                data.put("size", dsize);
                data.put("type", dtype);
                data.put("cssclass", dcssclass);
                data.put("onClick", donClick);
                data.put("model", dmodel);
                datas.add(data);
                columns += 1;
            }
            Repeater rep = new Repeater();
            rep.setHeaders(headers);
            rep.setData(datas);
            rep.setExtra(extra);
            rep.setLinks(links);
            repeaters.add(rep);
            index += 1;
        }
    }

    private void getVariables(XMLTag xmlTag, String className) {
        variables = new ArrayList<Map<String, String>>();
        int index = 1;
        while (true) {
            String name = xmlTag.rawXpathString(className + "/variable[" + index + "]/@name");
            String type = xmlTag.rawXpathString(className + "/variable[" + index + "]/@type");
            String style = xmlTag.rawXpathString(className + "/variable[" + index + "]/@style");
            String model = xmlTag.rawXpathString(className + "/variable[" + index + "]/model");
            String ajaxOnUpdate =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/ajaxOnUpdate");
            String ajaxOnEvent =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/ajaxOnEvent");
            String onSubmit =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/onSubmit");
            String onError = xmlTag.rawXpathString(className + "/variable[" + index + "]/onError");
            String onUpdate =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/onUpdate");
            String label = xmlTag.rawXpathString(className + "/variable[" + index + "]/@label");
            String size = xmlTag.rawXpathString(className + "/variable[" + index + "]/@size");
            String setOutputMarkupId =
                    xmlTag.rawXpathString(
                            className + "/variable[" + index + "]/@setOutputMarkupId");
            String readonly =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/@readonly");
            String title = xmlTag.rawXpathString(className + "/variable[" + index + "]/@title");
            String isEnabled =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/isEnabled");
            String onSelectionChanged =
                    xmlTag.rawXpathString(
                            className + "/variable[" + index + "]/onSelectionChanged");
            String afterInit0 =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/afterInit[1]");
            String afterInit1 =
                    xmlTag.rawXpathString(className + "/variable[" + index + "]/afterInit[2]");
            String defaultFormProcess =
                    xmlTag.rawXpathString(
                            className + "/variable[" + index + "]/@defaultFormProcess");
            String visible = xmlTag.rawXpathString(className + "/variable[" + index + "]/@visible");
            if (name.isEmpty()) {
                break;
            }
            Map<String, String> variable = new HashMap<String, String>();
            variable.put("name", name);
            variable.put("type", type);
            variable.put("model", model);
            variable.put("label", label);

            if ("TextField".equals(type) || "DateTextField".equals(type)) {
                variable.put("htmlType", "input");
                variable.put("style", "");
                variable.put("size", " size=\"" + size + "\"");
            } else if ("DictionaryTextField".equals(type)) {
                variable.put("htmlType", "input");
                variable.put(
                        "style",
                        style.isEmpty() ? " style=\"width:160px;\"" : " style=\"" + style + "\"");
                variable.put("size", "");
            } else if ("DropDownChoice".equals(type)) {
                variable.put("htmlType", "select");
                variable.put(
                        "style",
                        style.isEmpty() ? " style=\"width:160px;\"" : " style=\"" + style + "\"");
                variable.put("size", "");
            } else if (type.endsWith("Panel")) {
                variable.put("htmlType", "span");
            } else if ("CheckBox".equals(type) || "AjaxCheckBox".equals(type)) {
                variable.put("htmlType", "input");
                variable.put("style", "");
                variable.put("size", " type=\"checkbox\"");
            } else if ("AjaxLink".equals(type)
                    || "Link".equals(type)
                    || "AjaxSubmitLink".equals(type)) {
                variable.put("htmlType", "input");
                variable.put("style", "");
                variable.put("label", "");
                variable.put("size", " value=\"" + label + "\" type=\"submit\"");
            } else if ("TextArea".equals(type)) {
                variable.put("htmlType", "textarea");
                variable.put("style", style.isEmpty() ? "" : " style=\"" + style + "\"");
                variable.put("size", " rows=\"3\" cols=\"20\"");
            }
            variable.put("htmlId", name.replaceAll("\\.", "_"));
            variable.put("model", model);
            variable.put("ajaxOnUpdate", ajaxOnUpdate);
            variable.put("ajaxOnEvent", ajaxOnEvent);
            variable.put("onSubmit", onSubmit);
            variable.put("onError", onError);
            variable.put("onUpdate", onUpdate);
            variable.put("isEnabled", isEnabled);
            variable.put("onSelectionChanged", onSelectionChanged);
            variable.put("setOutputMarkupId", setOutputMarkupId);
            variable.put("readonly", readonly);
            variable.put("title", title);
            variable.put("afterInit0", afterInit0);
            variable.put("afterInit1", afterInit1);
            variable.put("defaultFormProcess", defaultFormProcess);
            variable.put("visible", visible);

            variables.add(variable);
            index += 1;
        }
    }

    private void getButtons(XMLTag xmlTag, String className) {
        buttons = new ArrayList<Map<String, String>>();
        int index = 1;
        while (true) {
            String name = xmlTag.rawXpathString(className + "/button[" + index + "]/@name");
            String type = xmlTag.rawXpathString(className + "/button[" + index + "]/@type");
            String label = xmlTag.rawXpathString(className + "/button[" + index + "]/@label");
            String title = xmlTag.rawXpathString(className + "/button[" + index + "]/@title");
            String readonly = xmlTag.rawXpathString(className + "/button[" + index + "]/@readonly");
            String onSubmit = xmlTag.rawXpathString(className + "/button[" + index + "]/text()");
            String afterInit0 =
                    xmlTag.rawXpathString(className + "/button[" + index + "]/afterInit[1]");
            if (name.isEmpty()) {
                break;
            }
            Map<String, String> button = new HashMap<String, String>();
            button.put("name", name);
            button.put("type", type);
            button.put("label", label);
            button.put("title", title);
            button.put("readonly", readonly);
            button.put("onSubmit", onSubmit.trim());
            button.put("afterInit0", afterInit0);
            buttons.add(button);
            index += 1;
        }
    }

    private List<Map<String, String>> getLinksList(XMLTag xmlTag, String parentPath) {
        List<Map<String, String>> links = new ArrayList<Map<String, String>>();
        int index = 1;
        while (true) {
            String name = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@name");
            String type = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@type");
            String label = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@label");
            String title = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@title");
            String readonly = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@readonly");
            String cssclass = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/@cssclass");
            String onClick = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/onClick");
            String model = xmlTag.rawXpathString(parentPath + "/link[" + index + "]/model");

            if (name.isEmpty()) {
                break;
            }
            Map<String, String> link = new HashMap<String, String>();
            link.put("name", name);
            link.put("type", type);
            link.put("label", label);
            link.put("readonly", readonly);
            link.put("title", title);
            link.put("cssclass", cssclass);
            link.put("onClick", onClick.trim());
            link.put("model", model.trim());
            links.add(link);
            index += 1;
        }
        return links;
    }
}
