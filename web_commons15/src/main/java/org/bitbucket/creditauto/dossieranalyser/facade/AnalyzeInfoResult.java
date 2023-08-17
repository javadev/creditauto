/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.dossieranalyser.facade;

import org.bitbucket.creditauto.helpers.GeneralResult;
import org.bitbucket.creditauto.wicket.InDossierStatus;

/**
 * AnalyzeInfoResult.
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class AnalyzeInfoResult extends GeneralResult {
    /**
     * Serilization/deserilization class back compatibillity.
     * */
    private static final long serialVersionUID = 500L;
    
    public String analysisProgress = "";
    public InDossierStatus statusOfDossier = InDossierStatus.UNDEFINED;
}
