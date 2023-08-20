package org.bitbucket.creditauto.authorization.facade;

import java.io.Serializable;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.User;

/**
 * AuthorizationResult.
 */
public class AuthorizationResult implements Serializable {
    private static final long serialVersionUID = 500L;

    /** isError. */
    public Boolean isError = Boolean.FALSE;
    /** if null value returned - avtorization failed. */
    public User user;
    /** User's shop. */
    public Externaldistributor userShop;
    /** Message or Error message for user will be show on login page. */
    public String messageKey;

    public void setErrorResult(String messageKey) {
        if (messageKey != null) {
            this.messageKey = messageKey;
            isError = true;
        }
    }
}
