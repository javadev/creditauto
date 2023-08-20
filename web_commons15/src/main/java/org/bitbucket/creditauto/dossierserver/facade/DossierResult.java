package org.bitbucket.creditauto.dossierserver.facade;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.bitbucket.creditauto.entity.In_instance;

public class DossierResult implements Serializable {
    private static final long serialVersionUID = 500L;
    
    public Boolean isError = Boolean.FALSE;
    // Message or Error message for user will be show on login page
    public String messageKey;

    public In_instance inInstance;

    public void setErrorResult(String messageKey) {
        if (messageKey != null) {
            this.messageKey = messageKey;
            isError = true;
        }
    }
}
