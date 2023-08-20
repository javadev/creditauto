/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class ByteDataRequestTarget extends ByteArrayResource implements IRequestTarget {
    private String fileName;

    /**
     * ByteDataRequestTarget constructor.
     *
     * <p>{@inheritDoc}
     *
     * @param mimeType type for the file
     * @param data actual content
     * @param fileName file name
     */
    public ByteDataRequestTarget(String mimeType, byte[] data, String fileName) {
        super(mimeType, data, fileName);
        this.fileName = fileName;
    }

    /** {@inheritDoc} */
    public void detach(RequestCycle requestCycle) {}

    /** {@inheritDoc} */
    public void respond(RequestCycle requestCycle) {
        requestCycle.setRequestTarget(
                new ResourceStreamRequestTarget(this.getResourceStream()) {
                    public String getFileName() {
                        return fileName;
                    }
                });
    }
}
