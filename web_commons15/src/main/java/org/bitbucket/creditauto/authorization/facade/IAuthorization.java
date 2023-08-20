/*
 * $Id$
 */
package org.bitbucket.creditauto.authorization.facade;

import org.bitbucket.creditauto.entity.User;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public interface IAuthorization {
    /**
     * Makes login and returns User object.
     * @param aLogin - login string
     * @param aPassword - password string
     * @param shopId - shop id string
     * @param aIP - IP address
     * @return the authorization result
     */
    AuthorizationResult login(String aLogin, String aPassword, String shopId, String aIP);

    /**
     * Changes password for user. Returns error in case of bad password.
     * @param forUser - user object
     * @param oldPassword - old password string
     * @param newPassword - new password string
     * @return the authorization result
     */
    AuthorizationResult changePassword(User forUser, String oldPassword, String newPassword);

    /**
     * Check the password for user.
     * @param forUser - user object
     * @return the password valid result
     */
    boolean isPasswordValid(User forUser);
}
