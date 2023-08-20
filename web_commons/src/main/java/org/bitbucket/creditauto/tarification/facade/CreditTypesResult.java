package org.bitbucket.creditauto.tarification.facade;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.bitbucket.creditauto.entity.Credittype;

/**
 * .
 */
public class CreditTypesResult implements Serializable {
    private static final long serialVersionUID = 500L;

    public Boolean isError = Boolean.FALSE;
    // if null value returned - error
    public List<Credittype> credittypes = Collections.emptyList();
    // Message or Error message for user will be show on login page
    public String messageKey;

    public void setErrorResult(String messageKey) {
        if (messageKey != null) {
            this.messageKey = messageKey;
            isError = true;
        }
    }
}
