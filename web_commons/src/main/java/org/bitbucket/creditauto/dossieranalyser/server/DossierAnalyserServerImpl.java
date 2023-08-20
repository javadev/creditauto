package org.bitbucket.creditauto.dossieranalyser.server;

import org.bitbucket.creditauto.dossieranalyser.facade.AnalyzeInfoResult;
import org.bitbucket.creditauto.dossieranalyser.facade.IDossierAnalyser;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.helpers.GeneralResult;
import org.bitbucket.creditauto.helpers.InstanceResult;
import org.bitbucket.creditauto.wicket.InDossierStatus;

/**
 * DossierAnalyserServerImpl.
 */
public class DossierAnalyserServerImpl implements IDossierAnalyser {
    public GeneralResult analyseDossier(
            In_instance inInstance,
            final String profile,
            final Boolean isOnceHandled,
            final InDossierStatus tobeStatus,
            final String user) {
        return new GeneralResult();
    }

    public AnalyzeInfoResult getAnalysisProgress(long ipsId, String user, boolean waitResult) {
        return new AnalyzeInfoResult();
    }

    public InstanceResult makeDecision(In_instance inInstance, String profile) {
        return new InstanceResult();
    }
}
