/*
 * $Id$
 */
package org.bitbucket.creditauto.helpers;

import org.bitbucket.creditauto.entity.In_instance;

/**
 * InstanceResult.
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class InstanceResult extends GeneralResult {
    /** Serilization/deserilization class back compatibillity. */
    private static final long serialVersionUID = 500L;

    public In_instance in_instance;
    public Boolean isTryToReduceStatus = Boolean.FALSE;
    public Boolean isDossierInProcess = Boolean.FALSE;
}
