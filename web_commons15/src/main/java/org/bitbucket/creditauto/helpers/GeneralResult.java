package org.bitbucket.creditauto.helpers;

import java.io.Serializable;

/**
 * GeneralResult.
 */
public class GeneralResult implements Serializable {

    /**
     * Serilization/deserilization class back compatibillity.
     */
    private static final long serialVersionUID = 500L;
    
    public Boolean isError = Boolean.FALSE;
    public String  messageKey = "";
    public String  localizedMessage = "";
    public Long    id;

    public void setErrorResult(String messageKey) {
        if (messageKey!=null) {
            this.messageKey = messageKey;
            isError = true;
        }
    }

    public static GeneralResult createErrorResult(String messageKey) {
        GeneralResult result = new GeneralResult();
        result.setErrorResult(messageKey);
        return result;
    }
}
