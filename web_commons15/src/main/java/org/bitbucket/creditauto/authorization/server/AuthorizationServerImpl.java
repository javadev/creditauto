/*
 * $Id$
 */
package org.bitbucket.creditauto.authorization.server;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.authorization.facade.AuthorizationResult;
import org.bitbucket.creditauto.authorization.facade.IAuthorization;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.wicket.JpaRequestCycle;

/**.
 * @author vko
 * @version $Revision$ $Date$
 */
public class AuthorizationServerImpl implements IAuthorization {

    public static final String DEFAULT_PASS = "Qwerty+1";
    /**
     * Makes login and returns User object.
     * @param aLogin - login string
     * @param aPassword - password string
     * @param shopId - the shop id
     * @param aIP - IP address
     * @return the authorization result
     */
    public AuthorizationResult login(String aLogin, String aPassword, String shopId, String aIP) {
        AuthorizationResult result = new AuthorizationResult();
        EntityManager em = null;
        for (int tryCount = 0; tryCount < 3; tryCount += 1) {
            try {
                em = JpaRequestCycle.get().getEntityManager();
                break;
            } catch (javax.persistence.PersistenceException ex) {
                if (tryCount == 3) {
                   result.setErrorResult("Ошибка соединения с базой данных");
                   return result;
                } else {
                    continue;
                }
            }
        }
        Query query = em.createQuery("select u from User u"
                + " where u.login = :login and u.password = :password");
        query.setParameter("login", aLogin);
        query.setParameter("password", aPassword);
        List<User> users = java.util.Collections.<User>emptyList();
        for (int tryCount = 0; tryCount < 3; tryCount += 1) {
            try {
                users = query.getResultList();
                break;
            } catch (javax.persistence.PersistenceException ex) {
                if (tryCount == 3) {
                   result.setErrorResult("Ошибка соединения с базой данных");
                   return result;
                } else {
                    continue;
                }
            }
        }
        if (users.isEmpty()) {
            LOG.error(this, "Login failed");
            result.setErrorResult("Пользователя с таким именем не существует или пароль введен неверно");
            return result;
        }
        User user = users.get(0);
        Externaldistributor userShop = null;
        for (Externaldistributor ed : user.getExternaldistributors()) {
            if (ed.getExt_id().equals(shopId)) {
                userShop = ed;
                break;
            }
        }
        if (userShop == null) {
            result.setErrorResult("Пользователь не зарегистрирован в этой торговой точке");
            return result;
        }
        result.user = user;
        result.userShop = userShop;
        return result;
    }

    /**
     * Changes password for user. Returns error in case of bad password.
     * @param forUser - user object
     * @param oldPassword - old password string
     * @param newPassword - new password string
     * @return the authorization result
     */
    public AuthorizationResult changePassword(User forUser, String oldPassword, String newPassword) {
        AuthorizationResult result = new AuthorizationResult();
        if (!forUser.getPassword().equals(oldPassword)) {
            result.setErrorResult("Неверный старый пароль");
            return result;
        }
        if (newPassword.equals(oldPassword)) {
            result.setErrorResult("Новый пароль не должен совпадать с текущим");
            return result;
        }
        if (newPassword.length() < 8 || newPassword.length() > 15
            || !newPassword.matches(".*[A-Za-z]+.*")
            || !newPassword.matches(".*[0-9]+.*")
            || !newPassword.matches(".*[.,\\-+/\\()#$%^&*!]+.*")
            || DEFAULT_PASS.equals(newPassword)) {
            result.setErrorResult("Неверный формат пароля");
            return result;
        }
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        forUser.setPassword(newPassword);
        forUser.setActivated(new Date());
        result.user = forUser;
        em.merge(result.user);
        return result;
    }

    public boolean isPasswordValid(User forUser) {
        return forUser.getActivated() != null;
    }
}
