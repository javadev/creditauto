/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File; 
import java.io.FileOutputStream; 
import java.util.Date; 
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import jxl.*;
import org.apache.commons.io.IOUtils;

/**
 * SqlGenerator.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class SqlGenerator {

    private final List<String> files;
    private final String outSql;
    private StringBuilder result;
    private String sheetName;
    private Cell[] headers;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    private String outcharset;
    private List<String> deleteCommands;

    /** Instance logger */
    private Log log;

    public SqlGenerator(List<String> files, String outSql, String outcharset) {
        this.files = files;
        this.outSql = outSql;
        this.outcharset = outcharset;
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
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
        readTables();
    }

    private void readTables() {
        result = new StringBuilder();
        try {
        // Scan xls files
        deleteCommands = new ArrayList<String>() {
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (int index = 0; index < size(); index += 1) {
                    sb.append(get(index));
                }
                return sb.toString();
            }
        };
        for (String fileName : files) {
            Workbook workbook = Workbook.getWorkbook(new File(fileName));
            List<String> sheetNames = new ArrayList<String>();
            // Scan sheets
            for (Sheet sheet : workbook.getSheets()) {
                sheetNames.add(sheet.getName());
                headers = sheet.getRow(0);
                sheetName = sheet.getName();
                appendDelete();
                // Scan rows
                for (int indexRow = 1; indexRow < sheet.getRows(); indexRow += 1) {
                    // Scan languages in row
                    for (String dictName : getDictNames()) {
                        appendHeaders(dictName, getDictNames().size() == 1);
                        Cell[] datas = sheet.getRow(indexRow);
                        // Scan columns in a row
                        if (datas != null && datas.length > 0) {
                            result.append(translateValue(0, cellToString(datas[0])));
                        }
                        for (int indexCell = 1; indexCell < headers.length; indexCell += 1) {
                            if (isValidDictHeader(headers[indexCell].getContents())) {
                                if (getDictNames().size() == 1) {
                                    result.append(", " + translateValue(indexCell, cellToString(datas[indexCell])));
                                } else if (!dictName.equals(headers[indexCell].getContents())) {
                                    result.append(", " + translateValue(indexCell, cellToString(datas[indexCell])));
                                }
                            }
                        }
                        if (isDict()) {
                            Calendar today = Calendar.getInstance();
                            Calendar from = (Calendar) today.clone();
                            Calendar to = (Calendar) today.clone();
                            from.add(Calendar.DATE, -1000);
                            to.add(Calendar.DATE, 1000);
                            result.append(", '" + sheetName.substring(5)
                                + "', 1, " + toSqlFormat(from) + ", " + toSqlFormat(to) + ", " + toSqlFormat(today));
                        }
                        result.append(")\n");
                    }
                }
            }
            workbook.close();
            getLog().info("read " + fileName.replaceFirst(".*/(.*)", "$1") + " - " + sheetNames);
        }
        IOUtils.write(deleteCommands.toString() + result.toString(), new FileOutputStream(outSql),
            outcharset == null || "".equals(outcharset) ? "cp1251" : outcharset) ;
        } catch (Exception ex) {
            getLog().error(ex);
        }
    }

    private String toSqlFormat(Calendar date) {
        return "'" + sdf.format(date.getTime()) + ".0'";
    }

    private void appendDelete() {
        if (isDict()) {
            deleteCommands.add("update " + getTableName() + " set valid=0 where name='" + sheetName.substring(5) + "'\n");
        }
    }

    private void appendHeaders(String dictName, boolean skipSheckDictName) {
        result.append("insert into " + getTableName() + " (");
        if (headers != null && headers.length > 0) {
            result.append(translateHeader(0));
        }
        for (int indexCell = 1; indexCell < headers.length; indexCell += 1) {
            if (isValidDictHeader(headers[indexCell].getContents())) {
                if (skipSheckDictName) {
                    result.append(", " + translateHeader(indexCell));
                } else if (!dictName.equals(headers[indexCell].getContents())) {
                    result.append(", " + translateHeader(indexCell));
                }
            }
        }
        if (isDict()) {
            result.append(", name, valid, fromdate, todate, lastmodificationdate");
        }
        result.append(") values (");
    }

    private List<String> getDictNames() {
        List<String> result = new ArrayList<String>();
        if (isDict()) {
            for (int indexCell = 0; indexCell < headers.length; indexCell += 1) {
                if (isValidDictLangHeader(headers[indexCell].getContents())) {
                    result.add(headers[indexCell].getContents());
                }
            } 
        } else {
            result.add("table@@@");
        }
        return result;
    }
    private String translateHeader(int indexHeader) {
        if ("russian".equals(headers[indexHeader].getContents()) || "ukrainian".equals(headers[indexHeader].getContents())) {
            return "dvalue, language";
        }
        return headers[indexHeader].getContents().replaceFirst("^key$", "dkey")
            .replaceFirst("^value$", "dvalue");
    }
    private String translateValue(int indexHeader, String value) {
        if ("russian".equals(headers[indexHeader].getContents())) {
            return value + ", 'ru'";
        } else if ("ukrainian".equals(headers[indexHeader].getContents())) {
            return value + ", 'uk'";
        }
        return value;
    }
    private boolean isValidDictHeader(String headerItem) {
        if (isDict()) {
            return headerItem.matches("(key|value|expkey|expkey2|expkey3|russian|ukrainian)");
        }
        return true;
    }
    private boolean isValidDictLangHeader(String headerItem) {
        return headerItem.matches("(russian|ukrainian)");
    }
    private boolean isDict() {
        return sheetName.startsWith("dict_");
    }

    private String getTableName() {
        return isDict() ? "dictionary_data" : sheetName;
    }

    private String cellToString(Cell cell) {
        if (cell.getType().equals(CellType.NUMBER)
            || cell.getType().equals(CellType.NUMBER_FORMULA)) {
            return cell.getContents().replaceFirst(",", ".").replaceFirst("%", "");
        } else if (cell.getType().equals(CellType.DATE)) {
            return "'" + dateFormatGmt.format(((DateCell) cell).getDate()) + "'";
        }
        return "'" + cell.getContents().replaceAll("'", "''") + "'";
    }
}
