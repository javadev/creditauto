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
 * Creates wicket java/html pages from xml file.
 *
 * @goal generate
 * @phase generate-sources
 * @author vko
 * @version $Revision$ $Date$
 */
public class Xml2wicket extends AbstractMojo {

    /** @parameter expression="${xmlFiles}" */
    private String xmlFiles;

    /** @parameter expression="${outPackage}" */
    private String outPackage;

    /** @parameter expression="${basedir}" */
    private String basedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("xml files:" + xmlFiles);
        getLog().info("out package:" + outPackage);
        if (xmlFiles == null) {
            throw new MojoExecutionException("xmlFiles is empty");
        }
        if (outPackage == null) {
            throw new MojoExecutionException("outPackage is empty");
        }

        String[] cmdArgs = xmlFiles.split(",");
        List<String> fileLocations = new ArrayList<String>();
        for (String file : cmdArgs) {
            fileLocations.add(basedir + "/src/main/datamodel/" + StringUtils.strip(file));
        }
        new WicketGenerator(basedir, fileLocations, outPackage).generate();

        getLog().info(outPackage + " generated.");
    }
}
