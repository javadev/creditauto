/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.In_asset;
import org.bitbucket.creditauto.entity.In_bloknot;
import org.bitbucket.creditauto.entity.In_document_store;
import org.bitbucket.creditauto.entity.In_dossier;
import org.bitbucket.creditauto.entity.In_good;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.entity.In_person;
import org.bitbucket.creditauto.entity.In_third_person;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
@SuppressWarnings({"unchecked"})
public final class InstanceHelper {

    private static java.util.Date emptyDate;

    static {
        try {
            emptyDate = new java.text.SimpleDateFormat("ddMMyyyy").parse("01011900");
        } catch (java.text.ParseException ex) {
            ex.getMessage();
        }
    }

    private InstanceHelper() {}

    public static List<In_instance> load(Date df, Date dt) {
        List<In_instance> result = new ArrayList<In_instance>();
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query queryInDossier =
                em.createQuery(
                        "select p from In_person p join p.in_dossier d "
                                + " where d.date_of_entering_dossier <= :dt and d.date_of_entering_dossier >= :df and p.dict_client_type = '1'");
        queryInDossier.setParameter("dt", dt);
        queryInDossier.setParameter("df", df);
        for (In_person inPerson : (List<In_person>) queryInDossier.getResultList()) {
            In_instance inInstance = new In_instance(true);
            inInstance.setIn_dossier(inPerson.getIn_dossier());
            inInstance.setIn_person(inPerson);
            result.add(inInstance);
        }
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        for (In_instance inInstance : result) {
            fillObjectDefaultValues(inInstance.getIn_person(), false);
            fillObjectDefaultValues(inInstance.getIn_person_partner(), false);
            fillObjectDefaultValues(inInstance.getIn_guarantor(), false);
            fillObjectDefaultValues(inInstance.getIn_guarantor_partner(), false);
            fillObjectDefaultValues(inInstance.getIn_goods(), false);
            fillObjectDefaultValues(inInstance.getIn_assets(), false);
        }

        return result;
    }

    public static In_instance load(Long id) {
        In_instance inInstance = new In_instance(false);
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query queryInDossier = em.createQuery("select d from In_dossier d where " + "d.id = :id");
        queryInDossier.setParameter("id", id);
        inInstance.setIn_dossier((In_dossier) queryInDossier.getResultList().get(0));
        loadInPerson(em, inInstance);
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        fillObjectDefaultValues(inInstance.getIn_person(), false);
        fillObjectDefaultValues(inInstance.getIn_person_partner(), false);
        fillObjectDefaultValues(inInstance.getIn_guarantor(), false);
        fillObjectDefaultValues(inInstance.getIn_guarantor_partner(), false);
        fillObjectDefaultValues(inInstance.getIn_goods(), false);
        fillObjectDefaultValues(inInstance.getIn_assets(), false);
        return inInstance;
    }

    private static void loadInPerson(EntityManager em, In_instance inInstance) {
        Query queryInPerson =
                em.createQuery("select p from In_person p where " + "p.in_dossier = :dossier_id");
        queryInPerson.setParameter("dossier_id", inInstance.getIn_dossier());
        for (In_person inPerson : (List<In_person>) queryInPerson.getResultList()) {
            if ("1".equals(inPerson.getDict_client_type())) {
                inInstance.setIn_person(inPerson);
            } else if ("2".equals(inPerson.getDict_client_type())) {
                inInstance.setIn_person_partner(inPerson);
            } else if ("3".equals(inPerson.getDict_client_type())) {
                inInstance.setIn_guarantor(inPerson);
            } else if ("4".equals(inPerson.getDict_client_type())) {
                inInstance.setIn_guarantor_partner(inPerson);
            }
        }
        Query queryInGoods =
                em.createQuery("select g from In_good g where " + "g.in_dossier = :dossier_id");
        queryInGoods.setParameter("dossier_id", inInstance.getIn_dossier());
        inInstance.setIn_goods((List<In_good>) queryInGoods.getResultList());
        Query queryInThirdPerson =
                em.createQuery(
                        "select dp from In_third_person dp where " + "dp.in_dossier = :dossier_id");
        queryInThirdPerson.setParameter("dossier_id", inInstance.getIn_dossier());
        inInstance.setIn_third_person((In_third_person) queryInThirdPerson.getResultList().get(0));
        Query queryInDocumentStore =
                em.createQuery(
                        "select ds from In_document_store ds where "
                                + "ds.in_dossier = :dossier_id");
        queryInDocumentStore.setParameter("dossier_id", inInstance.getIn_dossier());
        inInstance.setIn_document_stores(
                (List<In_document_store>) queryInDocumentStore.getResultList());
        Query queryInBlocknot =
                em.createQuery(
                        "select ib from In_bloknot ib where " + "ib.in_dossier = :dossier_id");
        queryInBlocknot.setParameter("dossier_id", inInstance.getIn_dossier());
        inInstance
                .getIn_dossier()
                .setIn_bloknots((List<In_bloknot>) queryInBlocknot.getResultList());
        Query queryInAssetStore =
                em.createQuery("select a from In_asset a where " + "a.in_dossier = :dossier_id");
        queryInAssetStore.setParameter("dossier_id", inInstance.getIn_dossier());
        inInstance.setIn_assets((List<In_asset>) queryInAssetStore.getResultList());
        inInstance.getIn_dossier().setIn_assets((List<In_asset>) queryInAssetStore.getResultList());
    }

    public static Long save(In_instance inInstance) {
        fillInInstance(inInstance);
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        if (em != null) {
            boolean isPersist = inInstance.getIn_dossier().getId() == null;
            saveObject(em, inInstance.getIn_dossier(), isPersist);
            saveObject(em, inInstance.getIn_person(), isPersist);
            saveObject(em, inInstance.getIn_person_partner(), isPersist);
            saveObject(em, inInstance.getIn_guarantor(), isPersist);
            saveObject(em, inInstance.getIn_guarantor_partner(), isPersist);
            for (In_good inGood : inInstance.getIn_goods()) {
                saveObject(em, inGood, isPersist);
            }
            saveObject(em, inInstance.getIn_third_person(), isPersist);
            for (In_document_store inDocumentStore : inInstance.getIn_document_stores()) {
                saveObject(em, inDocumentStore, isPersist);
            }

            // save good notes to db, remove redundant inbloknot notes:
            Map<Long, In_bloknot> notesMap = new HashMap<Long, In_bloknot>();
            for (In_bloknot inB : inInstance.getIn_dossier().getIn_bloknots()) {
                if (inB != null && inB.getText() != null) {
                    saveObject(em, inB, inB.getId() == null);
                }
                notesMap.put(inB.getId(), inB);
            }
            Query queryInBlocknot =
                    em.createQuery(
                                    "select ib from In_bloknot ib where "
                                            + "ib.in_dossier = :dossier_id")
                            .setParameter("dossier_id", inInstance.getIn_dossier());

            for (In_bloknot inB : (List<In_bloknot>) queryInBlocknot.getResultList()) {
                if (notesMap.get(inB.getId()) == null) {
                    removeObject(em, inB);
                }
            }
            for (In_asset inAsset : inInstance.getIn_assets()) {
                saveObject(em, inAsset, isPersist);
            }
            /*
                        //assets
                        Map<Long, In_asset> assetsMap = new HashMap<Long, In_asset>();
                        if (inInstance.getIn_dossier().getIn_assets() != null) {
                            for (In_asset inA : inInstance.getIn_dossier().getIn_assets()) {
                                if (inA != null && inA.getDictionary_asset() != null) {
                                    saveObject(em, inA, inA.getId() == null);
                                }
                                assetsMap.put(inA.getId(), inA);
                            }
                        }
                        Query queryInA = em.createQuery("select a from In_asset a where "
                                + "a.in_dossier = :dossier_id").setParameter("dossier_id", inInstance.getIn_dossier());

                        for (In_asset inA : (List<In_asset>) queryInA.getResultList()) {
                            if (assetsMap.get(inA.getId()) == null) {
                                removeObject(em, inA);
                            }
                        }
            */
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            LOG.info(null, "Document was saved, id=" + inInstance.getIn_dossier().getId());
        }
        undoFillInInstance(inInstance);
        return inInstance.getIn_dossier().getId();
    }

    private static void saveObject(EntityManager em, Object obj, boolean isPersist) {
        if (isPersist) {
            em.persist(obj);
        } else {
            em.merge(obj);
        }
    }

    private static void removeObject(EntityManager em, Object obj) {
        em.remove(obj);
    }

    /**
     * Copies reg address to the mail address.
     *
     * @param inPerson the person object
     */
    public static void copyRegToMail(In_person inPerson) {
        inPerson.setMailadditionalinfo(inPerson.getRegadditionalinfo());
        inPerson.setMailflat(inPerson.getRegflat());
        inPerson.setMailhouse(inPerson.getReghouse());
        inPerson.setMailstreet(inPerson.getRegstreet());
        inPerson.setDictionary_mailcity(inPerson.getDictionary_regcity());
        inPerson.setMailpostindex(inPerson.getRegpostindex());
        inPerson.setDictionary_mailprovince(inPerson.getDictionary_regprovince());
        inPerson.setDictionary_mailregion(inPerson.getDictionary_regregion());
    }

    /**
     * Copies mail address to the reg address.
     *
     * @param inPerson the person object
     */
    public static void copyMailToReg(In_person inPerson) {
        inPerson.setRegadditionalinfo(inPerson.getMailadditionalinfo());
        inPerson.setRegflat(inPerson.getMailflat());
        inPerson.setReghouse(inPerson.getMailhouse());
        inPerson.setRegstreet(inPerson.getMailstreet());
        inPerson.setDictionary_regcity(inPerson.getDictionary_mailcity());
        inPerson.setRegpostindex(inPerson.getMailpostindex());
        inPerson.setDictionary_regprovince(inPerson.getDictionary_mailprovince());
        inPerson.setDictionary_regregion(inPerson.getDictionary_mailregion());
    }

    private static void fillInInstance(In_instance inInstance) {
        if (inInstance.getIn_dossier().getCredittype() == null) {
            org.bitbucket.creditauto.entity.Credittype credittype =
                    new org.bitbucket.creditauto.entity.Credittype();
            credittype.setId(9999L);
            inInstance.getIn_dossier().setCredittype(credittype);
        }
        fillObjectDefaultValues(inInstance.getIn_person(), true);
        fillObjectDefaultValues(inInstance.getIn_person_partner(), true);
        fillObjectDefaultValues(inInstance.getIn_guarantor(), true);
        fillObjectDefaultValues(inInstance.getIn_guarantor_partner(), true);
        fillObjectDefaultValues(inInstance.getIn_goods().get(0), true);
        fillObjectDefaultValues(inInstance.getIn_assets(), true);
    }

    private static void undoFillInInstance(In_instance inInstance) {
        if (inInstance.getIn_dossier().getCredittype() != null
                && inInstance.getIn_dossier().getCredittype().getId() == 9999L) {
            inInstance.getIn_dossier().setCredittype(null);
        }
        fillObjectDefaultValues(inInstance.getIn_person(), false);
        fillObjectDefaultValues(inInstance.getIn_person_partner(), false);
        fillObjectDefaultValues(inInstance.getIn_guarantor(), false);
        fillObjectDefaultValues(inInstance.getIn_guarantor_partner(), false);
        fillObjectDefaultValues(inInstance.getIn_goods().get(0), false);
        fillObjectDefaultValues(inInstance.getIn_assets(), false);
    }

    private static void fillObjectDefaultValues(List<?> objects, boolean setDefault) {
        if (objects == null) {
            return;
        }
        for (Object object : objects) {
            fillObjectDefaultValues(object, setDefault);
        }
    }

    private static void fillObjectDefaultValues(Object object, boolean setDefault) {
        if (object == null) {
            return;
        }
        java.util.List<String> names = new java.util.ArrayList<String>();
        java.util.Map<String, Integer> namesValues = new java.util.HashMap<String, Integer>();
        for (java.lang.reflect.Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(javax.validation.constraints.NotNull.class)) {
                int min = -999;
                if (method.isAnnotationPresent(javax.validation.constraints.Min.class)
                        && setDefault) {
                    min =
                            (int)
                                    method.getAnnotation(javax.validation.constraints.Min.class)
                                            .value();
                }
                String propertyName = method.getName().replaceFirst("get", "");
                propertyName =
                        propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                namesValues.put(propertyName, min);
                names.add(propertyName);
            }
        }
        for (String property : names) {
            Object oldValue = null;
            try {
                java.lang.reflect.Field fld = object.getClass().getDeclaredField(property);
                fld.setAccessible(true);
                oldValue = fld.get(object);
                if (setDefault) {
                    if (oldValue == null && fld.getType().equals(String.class)) {
                        fld.set(object, "-");
                    } else if (oldValue == null && fld.getType().equals(java.util.Date.class)) {
                        fld.set(object, emptyDate);
                    } else if (oldValue == null && fld.getType().equals(Integer.class)) {
                        fld.set(object, namesValues.get(property));
                    }
                } else {
                    if (fld.getType().equals(String.class) && "-".equals(oldValue)) {
                        fld.set(object, null);
                    } else if (fld.getType().equals(java.util.Date.class)
                            && emptyDate.equals(oldValue)) {
                        fld.set(object, null);
                    } else if (fld.getType().equals(Integer.class)
                            && namesValues.get(property).equals(oldValue)
                            && namesValues.get(property) == -999) {
                        fld.set(object, null);
                    }
                }
            } catch (NoSuchFieldException ex) {
                LOG.error(null, ex.getMessage());
            } catch (IllegalAccessException ex) {
                LOG.error(null, ex.getMessage());
            }
        }
    }
}
