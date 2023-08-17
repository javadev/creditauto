/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;

/**
 * Creates Xml Schema Document based on the input xml files.
 *
 * @goal generate
 * @phase generate-sources
 * @author vko
 * @version $Revision$ $Date$
 */
public class Sql2java extends AbstractMojo {

    /**
     * @parameter expression="${sqlFiles}"
     */
    private String sqlFiles;

    /**
     * @parameter expression="${outPackage}"
     */
    private String outPackage;

    /**
     * @parameter expression="${aliases}"
     */
    private String aliases;

    /**
     * @parameter expression="${basedir}"
     */
    private String basedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("sql files:" + sqlFiles);
        getLog().info("out package:" + outPackage);
        getLog().info("aliases:" + aliases);

        String[] cmdArgs = sqlFiles.split(",");
        List<String> fileLocations = new ArrayList<String>();
        for (String file : cmdArgs) {
            fileLocations.add(basedir + "/src/main/schema/" + StringUtils.strip(file));
        }
        new JavaGenerator(basedir, fileLocations, outPackage, aliases.split(",")).generate();

        getLog().info(outPackage + " generated.");
    }
}
