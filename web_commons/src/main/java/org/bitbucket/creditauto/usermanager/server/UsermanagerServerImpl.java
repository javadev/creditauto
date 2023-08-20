/*
 * $Id$
 *
 * Copyright (c) 2011 (alisa)
 */
package org.bitbucket.creditauto.usermanager.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.Powerofattorney;
import org.bitbucket.creditauto.entity.Urole;
import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.entity.User_has_externaldistributor;
import org.bitbucket.creditauto.usermanager.facade.IUsermanager;
import org.bitbucket.creditauto.wicket.JpaRequestCycle;
import org.bitbucket.creditauto.wicket.SearchData.AllUsers;

/**
 * .
 *
 * @author alisa
 * @version $Revision$ $Date$
 */
public class UsermanagerServerImpl implements IUsermanager {
    private static final String WHERE = " where ";
    private static final String AND = " and ";
    public static final String DEFAULT_PASSWORD = "Qwerty+1";
    public static final Long ERROR_CODE_1 = Long.valueOf(-1);
    public static final Long ERROR_CODE_2 = Long.valueOf(-2);

    public List<Externaldistributor> getExternaldistributors() {
        List<Externaldistributor> res;
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query shops = em.createQuery("select x from Externaldistributor x");
        res = shops.getResultList();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return res;
    }

    public List<User> getUsers(AllUsers usersSearchCriteria) {
        Map<String, Powerofattorney> attMap = new HashMap<String, Powerofattorney>();
        List<User> result;
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qUsersStr =
                new StringBuffer("select u from User u left join fetch  u.powerofattorneys  ");
        Query queryUsers;
        if (usersSearchCriteria != null) {
            // users
            if (usersSearchCriteria.getUserLogin() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" UPPER(u.login) = :login");
            }
            if (usersSearchCriteria.getUserName() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" UPPER(u.name) like :name");
            }
            if (usersSearchCriteria.getUserTableNumber() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" UPPER(u.table_number) = :tnumber");
            }
            // power of attor criteria
            if (usersSearchCriteria.getPowerOfAttNumber() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" u.powerofattorneys.attorney_number = :number");
            }
            if (usersSearchCriteria.getPowerOfAttStart() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" u.powerofattorneys.attorney_date_start >= :start");
            }
            if (usersSearchCriteria.getPowerOfAttFinish() != null) {
                qUsersStr.append(!qUsersStr.toString().contains(WHERE) ? WHERE : AND);
                qUsersStr.append(" u.powerofattorneys.attorney_date_finish <= :end");
            }
            queryUsers = em.createQuery(qUsersStr.toString());

            if (usersSearchCriteria.getUserLogin() != null) {
                queryUsers.setParameter("login", usersSearchCriteria.getUserLogin().toUpperCase());
            }
            if (usersSearchCriteria.getUserName() != null) {
                queryUsers.setParameter(
                        "name", "%" + usersSearchCriteria.getUserName().toUpperCase() + "%");
            }
            if (usersSearchCriteria.getUserTableNumber() != null) {
                queryUsers.setParameter(
                        "tnumber", usersSearchCriteria.getUserTableNumber().toUpperCase());
            }
            if (usersSearchCriteria.getPowerOfAttNumber() != null) {
                queryUsers.setParameter("number", usersSearchCriteria.getPowerOfAttNumber());
            }
            if (usersSearchCriteria.getPowerOfAttStart() != null) {
                queryUsers.setParameter("start", usersSearchCriteria.getPowerOfAttStart());
            }
            if (usersSearchCriteria.getPowerOfAttFinish() != null) {
                queryUsers.setParameter("end", usersSearchCriteria.getPowerOfAttFinish());
            }
        } else {
            queryUsers = em.createQuery(qUsersStr.toString());
            return (List<User>) queryUsers.getResultList();
        }
        List<User> res = (List<User>) queryUsers.getResultList();
        for (User u : res) {
            u.setExternaldistributors(getUserExternaldistributors(u));
        }
        return res;
    }

    public Long save(User user) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {
            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(this, "user was saved, id=" + user.getId());
        }
        return user.getId();
    }

    public User getUserWithRoles(User user) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qUsersStr =
                new StringBuffer("select u from User u left join fetch  u.uroles ");
        qUsersStr.append(" where u.id = :id");
        Query query = em.createQuery(qUsersStr.toString());
        query.setParameter("id", user.getId());
        User res = (User) query.getSingleResult();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return res;
    }

    public Long deleteUserRole(User user, Long id) {

        Map<Long, Urole> mapRoles = new HashMap<Long, Urole>();
        for (Urole role : getAllRoles()) {
            mapRoles.put(role.getId(), role);
        }
        if (!mapRoles.get(id).getUsers().contains(user)) {
            // user has no this role
            return null;
        }
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {
            mapRoles.get(id).getUsers().remove(user);
            em.getTransaction().begin();
            em.merge(mapRoles.get(id));
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(this, "user_role was deleted");

            return 0L;
        } else {
            return null;
        }
    }

    public Long addUserRole(User user, Long id) {
        User user2 = getUserWithRoles(user);
        for (Urole role : user2.getUroles()) {
            if (role.getId().equals(id)) {
                // already has this role
                return null;
            }
        }
        Map<Long, Urole> mapRoles = new HashMap<Long, Urole>();
        for (Urole role : getAllRoles()) {
            mapRoles.put(role.getId(), role);
        }
        // else
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {

            mapRoles.get(id).getUsers().add(user2);
            em.getTransaction().begin();
            em.merge(mapRoles.get(id));
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(this, "user_role was saved");

            return 0L;
        } else {
            return null;
        }
    }

    public List<Urole> getAllRoles() {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qStr = new StringBuffer("select r from Urole r order by r.id desc ");
        Query query = em.createQuery(qStr.toString());
        List<Urole> res = (List<Urole>) query.getResultList();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return res;
    }

    public List<Externaldistributor> getUserExternaldistributors(User user) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qUsersStr =
                new StringBuffer(
                        "select x from Externaldistributor x"
                                + " where x.id in (select ue.externaldistributor_id from User_has_externaldistributor ue"
                                + " where ue.user_id = :user_id and (ue.active is null or ue.active = true)) order by x.ext_id");
        Query query = em.createQuery(qUsersStr.toString());
        query.setParameter("user_id", user.getId());
        List<Externaldistributor> res = (List<Externaldistributor>) query.getResultList();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return res;
    }

    public List<Powerofattorney> getUserPowerofattorneys(User user) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qUsersStr = new StringBuffer("select p from Powerofattorney p ");
        qUsersStr.append(" where p.user = :user");
        qUsersStr.append(
                " and (p.attorney_date_finish is null or p.attorney_date_finish >= :today) ");
        qUsersStr.append(" order by p.id desc ");
        Query query = em.createQuery(qUsersStr.toString());
        query.setParameter("user", user);
        query.setParameter("today", new Date());
        return (List<Powerofattorney>) query.getResultList();
    }

    public Long save(Powerofattorney pow) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {
            if (pow.getId() == null) {
                em.persist(pow);
            } else {
                em.merge(pow);
            }

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(this, "Powerofattorney was saved, id=" + pow.getId());
        }
        return pow.getId();
    }

    public Long addUserExternaldistributor(User user, String extId) {
        // check if ext_id is correct
        Map<String, Externaldistributor> shopsMap = new HashMap<String, Externaldistributor>();
        for (Externaldistributor shop : getExternaldistributors()) {
            shopsMap.put(shop.getExt_id(), shop);
        }
        if (shopsMap.get(extId) == null) {
            return ERROR_CODE_1;
        }
        // check if there is no such shop or a User
        Map<String, Externaldistributor> userShopsMap = new HashMap<String, Externaldistributor>();
        for (Externaldistributor shop : getUserExternaldistributors(user)) {
            userShopsMap.put(shop.getExt_id(), shop);
        }
        if (userShopsMap.get(extId) != null) {
            return ERROR_CODE_2;
        }

        // if all previous is ok then add shop
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {

            user.getExternaldistributors().set(0, shopsMap.get(extId));
            em.getTransaction().begin();
            em.merge(user);
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(this, "user_has_externaldistributor was saved");

            return 0L;
        } else {
            return null;
        }
    }

    public Long deleteUserExternaldistributor(User user, Externaldistributor shop) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();

        Query q =
                em.createQuery(
                        "select u from User_has_externaldistributor u where "
                                + "u.user_id = :user_id and u.externaldistributor_id = :externaldistributor_id "
                                + "and (u.active is null or u.active = true)");
        q.setParameter("user_id", user.getId());
        q.setParameter("externaldistributor_id", shop.getId());
        User_has_externaldistributor uhase =
                (q.getResultList() != null && !q.getResultList().isEmpty())
                        ? (User_has_externaldistributor) q.getResultList().get(0)
                        : null;
        if (uhase == null) {
            return null;
        }
        uhase.setActive(Boolean.FALSE);
        em.merge(uhase);
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        LOG.info(this, "user_has_externaldistributor was updated , id=" + uhase.getId());
        return uhase.getId();
    }

    public Long deleteUserPowerofattorney(User user, Powerofattorney pow) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        StringBuffer qUsersStr = new StringBuffer("select p from Powerofattorney p ");
        qUsersStr.append(" where p.user = :user and p.id = :id");
        Query q = em.createQuery(qUsersStr.toString());
        q.setParameter("user", user);
        q.setParameter("id", pow.getId());

        Powerofattorney result =
                (q.getResultList() != null && !q.getResultList().isEmpty())
                        ? (Powerofattorney) q.getResultList().get(0)
                        : null;
        if (result == null) {
            return null;
        }
        result.setAttorney_date_finish(new Date());
        em.merge(result);
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        LOG.info(this, "Powerofattorney was updated , id=" + result.getId());
        return result.getId();
    }
}
