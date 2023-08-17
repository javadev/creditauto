/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;

/**
 * Creates Sql script from xls file.
 *
 * @goal generate
 * @phase generate-sources
 * @author vko
 * @version $Revision$ $Date$
 */
public class Xls2sql extends AbstractMojo {

    /**
     * @parameter expression="${xlsFiles}"
     */
    private String xlsFiles;

    /**
     * @parameter expression="${outSql}"
     */
    private String outSql;

    /**
     * @parameter expression="${basedir}"
     */
    private String basedir;

    /**
     * @parameter outcharset="${outcharset}"
     */
    private String outcharset;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("xls files:" + xlsFiles);
        getLog().info("out sql:" + outSql);
        if (xlsFiles == null) {
            throw new MojoExecutionException("xlsFiles is empty");
        }
        if (outSql == null) {
            throw new MojoExecutionException("outSql is empty");
        }

        String[] cmdArgs = xlsFiles.split(",");
        List<String> fileLocations = new ArrayList<String>();
        for (String file : cmdArgs) {
            fileLocations.add(basedir + "/src/main/datamodel/" + StringUtils.strip(file));
        }
        new SqlGenerator(fileLocations, outSql, outcharset).generate();

        getLog().info(outSql + " generated.");
    }
}
