/*
 * $Id$
 */
package org.bitbucket.creditauto.dossieranalyser.facade;

import org.bitbucket.creditauto.helpers.GeneralResult;
import org.bitbucket.creditauto.helpers.InstanceResult;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.wicket.InDossierStatus;

public interface IDossierAnalyser {
    /**
     * On some stages when we go through user work flow to create deal
     * we should put our deal on special complicated processing to be handled
     * by dossier analyser server in asynchronous mode. This method is designed to do it.
     * @param inInstance - In_instance object to be processed
     * @param profile - profile number/name defines kind of particular processing
     * @param isOnceHandled - if this profile must be processed only once during deal life cycle
     * @param tobeStatus - Status name to be assigned for deal during processing and before
     * dossier analyser assigns final status according appropriate processing defined by profile name/number
     * @param user - user name/login just for log reasons
     * @return unified result object
     */
    GeneralResult analyseDossier(In_instance inInstance, final String profile, final Boolean isOnceHandled, final InDossierStatus tobeStatus, final String user);

    /**
     * Waits for process and gets analize processing result
     * @param ipsId - ips id
     * @param user - user name/login
     * @param waitResult - boolean value for waitResult
     * @return processing result
     */
    AnalyzeInfoResult getAnalysisProgress(long ipsId, String user, boolean waitResult);

    /**
     * It works for any profile.
     * @param inInstance - instance to be processed to make a decision by expert system
     * @param profile the profile
     * @return result of this processing
     */
    InstanceResult makeDecision(In_instance inInstance, String profile);
}
