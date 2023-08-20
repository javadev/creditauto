/*
 * $Id$
 */
package org.bitbucket.creditauto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

/**
 * JavaGenerator.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class JavaGenerator {
    private static final Pattern TABLE_HEADER =
            Pattern.compile("CREATE  TABLE IF NOT EXISTS `mydb`.`(.*?)` \\(");
    private static final Pattern TABLE_FOOTER = Pattern.compile(".*?PRIMARY KEY.*?\\);");
    private static final Pattern TABLE_FOOTER2 = Pattern.compile("^';");
    private static final Pattern COLUMN_WITH_COMMENT =
            Pattern.compile(
                    "  `(\\S*?)` (\\S*?) (NOT\\s{0,1})*NULL (AUTO_INCREMENT )*COMMENT '(.*?)'.*");
    private static final Pattern COLUMN_WITHOUT_COMMENT =
            Pattern.compile("  `(\\S*?)` (\\S*?) (NOT\\s{0,1})*NULL.*");
    private static final Pattern FOREIGN_KEY =
            Pattern.compile("    FOREIGN KEY \\(`(\\S*?)` \\).*");
    private static final Pattern TABLE_COMMENT = Pattern.compile("^COMMENT = '$");
    private static final Pattern TABLE_NAME_WITH_HAS = Pattern.compile("^(\\w+?)_has_(\\w+?)$");
    private final String basedir;
    private final List<String> files;
    private final String outPackage;
    private List<String> lines;
    private Map<String, List<String>> tables;
    private Map<String, List<Map<String, String>>> columns;
    private String[] aliases;

    /** Instance logger */
    private Log log;

    public JavaGenerator(String basedir, List<String> files, String outPackage, String[] aliases) {
        this.basedir = basedir;
        this.files = files;
        this.outPackage = outPackage;
        this.aliases = aliases;
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
        readLines();
        scanTables();
        scanColumns();
        scanManyToOne();
        scanManyToMany();
        generateJava();
        generateJavaInstance();
    }

    private void readLines() {
        lines = new ArrayList<String>();
        try {
            for (String fileName : files) {
                getLog().info("read file - " + fileName);
                BufferedReader input =
                        new BufferedReader(
                                new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
                try {
                    String line = input.readLine();
                    int count = 0;
                    while (line != null) {
                        lines.add(line);
                        line = input.readLine();
                        count += 1;
                    }
                } finally {
                    input.close();
                }
            }
        } catch (IOException ex) {
            getLog().error(ex);
        }
    }

    private void scanTables() {
        tables = new TreeMap<String, List<String>>();
        String tableName = null;
        for (String line : lines) {
            Matcher matcher = TABLE_HEADER.matcher(line);
            if (matcher.matches()) {
                tableName = matcher.group(1);
                for (String alias : aliases) {
                    if (tableName.equals(alias.replaceFirst("(.*?):(.*)", "$1"))) {
                        tableName = alias.replaceFirst("(.*?):(.*)", "$2");
                    }
                }
                tables.put(tableName, new ArrayList<String>());
                continue;
            }
            if (TABLE_FOOTER.matcher(line).matches() || TABLE_FOOTER2.matcher(line).matches()) {
                tableName = null;
            }
            if (tableName != null) {
                tables.get(tableName).add(line);
            }
        }
        getLog().info("Generate tables: " + getKeys(tables));
    }

    private void scanColumns() {
        columns = new TreeMap<String, List<Map<String, String>>>();
        Iterator<String> tableKey = tables.keySet().iterator();
        while (tableKey.hasNext()) {
            String tableName = tableKey.next();
            for (String alias : aliases) {
                if (tableName.equals(alias.replaceFirst("(.*?):(.*)", "$1"))) {
                    tableName = alias.replaceFirst("(.*?):(.*)", "$2");
                }
            }
            columns.put(tableName, new ArrayList<Map<String, String>>());
            boolean commentActive = false;
            String commentTable = "";
            for (String line : tables.get(tableName)) {
                Matcher lineWithComment = COLUMN_WITH_COMMENT.matcher(line);
                if (lineWithComment.matches()) {
                    Map<String, String> item = new TreeMap<String, String>();
                    item.put("name", lineWithComment.group(1));
                    item.put("type", lineWithComment.group(2));
                    if (item.get("type").toLowerCase().startsWith("varchar")) {
                        item.put(
                                "length",
                                lineWithComment.group(2).replaceFirst(".*?\\((\\d+)\\)", "$1"));
                    }
                    item.put(
                            "nullable",
                            Boolean.valueOf(lineWithComment.group(3) == null).toString());
                    String comment =
                            lineWithComment.group(5).replaceFirst("(^.*?)@.*", "$1").trim();
                    if (!"".equals(comment)) {
                        item.put("comment", comment.replaceAll("\\\\n", ""));
                    }
                    int index = 0;
                    boolean sizePresent = false;
                    boolean checkPresent = false;
                    if (lineWithComment.group(5).contains("@")) {
                        for (String annotItem :
                                lineWithComment
                                        .group(5)
                                        .replaceAll("\\\\n", " ")
                                        .replaceFirst("^.*?@", "")
                                        .split(" @")) {
                            item.put(
                                    "annotation" + index,
                                    annotItem
                                            .replaceAll("\\\\\"", "\"")
                                            .replaceAll("\\\\\\\\", "\\\\"));
                            if (!sizePresent) {
                                sizePresent = annotItem.contains("Size");
                            }
                            if (!checkPresent) {
                                checkPresent = annotItem.contains("Check");
                            }
                            index += 1;
                        }
                    }
                    if (item.get("length") != null
                            && !item.get("length").isEmpty()
                            && !sizePresent) {
                        item.put("annotation" + index, "Size(max = " + item.get("length") + ")");
                        index += 1;
                    }
                    if (item.get("type").toLowerCase().startsWith("decimal") && !checkPresent) {
                        item.put("annotation" + index, "Check");
                    } else if (item.get("type").toLowerCase().startsWith("int") && !checkPresent) {
                        item.put("annotation" + index, "Check");
                    }
                    columns.get(tableName).add(item);
                    continue;
                }
                Matcher lineWithoutComment = COLUMN_WITHOUT_COMMENT.matcher(line);
                if (lineWithoutComment.matches()) {
                    Map<String, String> item = new TreeMap<String, String>();
                    item.put("name", lineWithoutComment.group(1));
                    item.put("type", lineWithoutComment.group(2));
                    if (item.get("type").toLowerCase().startsWith("varchar")) {
                        item.put(
                                "length",
                                lineWithoutComment.group(2).replaceFirst(".*?\\((\\d+)\\)", "$1"));
                    }
                    item.put(
                            "nullable",
                            Boolean.valueOf(lineWithoutComment.group(3) == null).toString());
                    columns.get(tableName).add(item);
                    int index = 0;
                    if (item.get("length") != null && !item.get("length").isEmpty()) {
                        item.put("annotation0", "Size(max = " + item.get("length") + ")");
                        index += 1;
                    }
                    if (item.get("type").toLowerCase().startsWith("decimal")) {
                        item.put("annotation" + index, "Check");
                    } else if (item.get("type").toLowerCase().startsWith("int")) {
                        item.put("annotation" + index, "Check");
                    }
                }
                if (!tableName.contains("_has_")) {
                    Matcher foreignKey = FOREIGN_KEY.matcher(line);
                    if (foreignKey.matches()) {
                        for (Map<String, String> item : columns.get(tableName)) {
                            if (item.get("name").equals(foreignKey.group(1))) {
                                item.put(
                                        "name",
                                        foreignKey
                                                .group(1)
                                                .replaceFirst("(\\w+)_.*", "$1")
                                                .toLowerCase());
                                String type = foreignKey.group(1).replaceFirst("(\\w+)_.*", "$1");
                                type = type.substring(0, 1).toUpperCase() + type.substring(1);
                                item.put("type", type);
                                item.put("annotation0", "ManyToOne");
                                item.put(
                                        "annotation1",
                                        "JoinColumn(name=\"" + item.get("name") + "_id\")");
                                item.put("annotation2", "NotNull");
                            }
                        }
                    }
                }
                if (TABLE_COMMENT.matcher(line).matches()) {
                    commentActive = true;
                    continue;
                }
                if (commentActive) {
                    commentTable += line + "\n";
                }
            }
            Collections.sort(
                    columns.get(tableName),
                    new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> o1, Map<String, String> o2) {
                            return o1.get("name").compareTo(o2.get("name"));
                        }
                    });
            if (!"".equals(commentTable)) {
                Map<String, String> item = new TreeMap<String, String>();
                item.put(
                        "commentTable",
                        commentTable
                                .replaceAll("\\\\n", " ")
                                .replaceFirst("(^.*?)@.*", "$1")
                                .trim());
                columns.get(tableName).add(0, item);
                int index = 0;
                if (commentTable.contains("@")) {
                    for (String annotItem :
                            commentTable
                                    .replaceAll("\\\\n", " ")
                                    .replaceFirst("^.*?@", "")
                                    .split(" @")) {
                        item.put(
                                "annotation" + index,
                                annotItem
                                        .replaceAll("[\\d\\n]", " ")
                                        .replaceAll("\\\\\"", "\"")
                                        .replaceAll("\\\\\\\\", "\\\\")
                                        .trim());
                        index += 1;
                    }
                }
            }
        }
    }

    private void scanManyToOne() {
        Iterator<String> tableKey = tables.keySet().iterator();
        while (tableKey.hasNext()) {
            String tableName = tableKey.next();
            for (Map<String, String> item : columns.get(tableName)) {
                if ("ManyToOne".equals(item.get("annotation0"))) {
                    List<Map<String, String>> tableColumns = columns.get(item.get("name"));
                    Map<String, String> newItem = new TreeMap<String, String>();
                    newItem.put("name", tableName + "s");
                    newItem.put(
                            "type",
                            "List<"
                                    + tableName.substring(0, 1).toUpperCase()
                                    + tableName.substring(1)
                                    + ">");
                    newItem.put("annotation0", "OneToMany(mappedBy=\"" + item.get("name") + "\")");
                    tableColumns.add(newItem);
                }
            }
        }
    }

    private void scanManyToMany() {
        Iterator<String> tableKey = tables.keySet().iterator();
        while (tableKey.hasNext()) {
            String tableName = tableKey.next();
            if (tableName.contains("_has_")) {
                Matcher tableNameWithHas = TABLE_NAME_WITH_HAS.matcher(tableName);
                if (tableNameWithHas.matches()) {
                    if (!fillManyToMany(
                            tableName, tableNameWithHas.group(1), tableNameWithHas.group(2))) {
                        continue;
                    }
                    if (!fillManyToMany(
                            tableName, tableNameWithHas.group(2), tableNameWithHas.group(1))) {
                        continue;
                    }
                }
            }
        }
    }

    private boolean fillManyToMany(String tableName, String tableName1, String tableName2) {
        for (String alias : aliases) {
            if (tableName1.equals(alias.replaceFirst(".*?_has_(.*?):.*?_has_(.*)", "$2"))) {
                tableName1 = alias.replaceFirst(".*?_has_(.*?):.*?_has_(.*)", "$1");
            }
            if (tableName2.equals(alias.replaceFirst(".*?_has_(.*?):.*?_has_(.*)", "$2"))) {
                tableName2 = alias.replaceFirst(".*?_has_(.*?):.*?_has_(.*)", "$1");
            }
        }
        List<Map<String, String>> items = columns.get(tableName1);
        for (Map<String, String> item : items) {
            if (item.get("name") != null && item.get("name").equals(tableName2 + "s")) {
                return false;
            }
        }
        Map<String, String> item = new TreeMap<String, String>();
        item.put("name", tableName2 + "s");
        item.put(
                "type",
                "List<" + tableName2.substring(0, 1).toUpperCase() + tableName2.substring(1) + ">");
        item.put("annotation0", "ManyToMany(cascade=CascadeType.ALL)");
        item.put(
                "annotation1",
                "JoinTable(name = \""
                        + tableName
                        + "\",\n"
                        + "          joinColumns = @JoinColumn(name = \""
                        + tableName1
                        + "_id\"),\n"
                        + "          inverseJoinColumns = @JoinColumn(name = \""
                        + tableName2
                        + "_id\"))");
        items.add(item);
        return true;
    }

    private void generateJava() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty(
                "resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        Velocity.init(p);

        Template template = getTemplate("sample.vm");

        Iterator<String> tableKey = tables.keySet().iterator();
        while (tableKey.hasNext()) {
            String tableName = tableKey.next();
            String className =
                    tableName.substring(0, 1).toUpperCase()
                            + tableName.substring(1).replaceFirst("(^.*?)@.*", "$1");
            VelocityContext context = new VelocityContext();
            context.put("className", className);
            context.put("tableName", tableName.replaceFirst("(^.*?)@(.*)", "$2"));
            context.put("columns", columns.get(tableName));
            context.put("outPackage", outPackage);
            try {
                String outDirs = basedir + "/src/main/java/" + outPackage.replaceAll("\\.", "/");
                new File(outDirs).mkdirs();
                Writer writer =
                        new OutputStreamWriter(
                                new FileOutputStream(outDirs + "/" + className + ".java"), "utf-8");
                template.merge(context, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                getLog().error(e);
            }
        }
    }

    private void generateJavaInstance() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty(
                "resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        Velocity.init(p);

        Template template = getTemplate("instance.vm");

        VelocityContext context = new VelocityContext();
        context.put("className", "In_instance");
        context.put("columns", columns);
        context.put("outPackage", outPackage);
        try {
            String outDirs = basedir + "/src/main/java/" + outPackage.replaceAll("\\.", "/");
            new File(outDirs).mkdirs();
            Writer writer =
                    new OutputStreamWriter(
                            new FileOutputStream(outDirs + "/" + "In_instance.java"), "utf-8");
            template.merge(context, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    /**
     * Get a template from the cache; if template has not been loaded, load it
     *
     * @param templatePath
     * @return
     */
    private static Template getTemplate(final String templatePath) {
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
    private static String getTemplateFromResource(final String templatePath) {
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

    public static <T, E> Set<T> getKeys(Map<T, E> map) {
        Set<T> keys = new LinkedHashSet<T>();
        for (Entry<T, E> entry : map.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}
