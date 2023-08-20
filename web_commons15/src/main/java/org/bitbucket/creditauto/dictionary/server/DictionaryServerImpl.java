/*
 * $Id$
 */
package org.bitbucket.creditauto.dictionary.server;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.Dictionary_data;
import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.wicket.JpaRequestCycle;

/**.
 * @author alisa
 * @author vko
 * @version $Revision$ $Date$
 */
public class DictionaryServerImpl implements IDictionary {
    // Setup 2 hour cache with 30 seconds check interval for 10000 items
    private static Cache<String, List<Dictionary_data>> cached = new Cache<String, List<Dictionary_data>>(7200, 30, 10000);

    public List<Dictionary_data> getDictionary(String dictionaryName, String language, Date date) {
        List<Dictionary_data> result = cached.get(dictionaryName + language);
        if (result == null) {
            result = getDictionary(dictionaryName, null, language, date, null, null);
            cached.put(dictionaryName + language, result);
        }
        return result;
    }

    public List<Dictionary_data> getDictionary(String dictionaryName, String expkey, String language, Date date) {
        List<Dictionary_data> result = cached.get(dictionaryName + language + expkey);
        if (result == null) {
            result = getDictionary(dictionaryName, null, language, date, expkey, null);
            cached.put(dictionaryName + language + expkey, result);
        }
        return result;
    }

    public List<Dictionary_data> getDictionary(String dictionaryName, String expkey, String expkey2, String language, Date date) {
        List<Dictionary_data> result = cached.get(dictionaryName + language + expkey + expkey2);
        if (result == null) {
            result = getDictionary(dictionaryName, null, language, date, expkey, expkey2);
            cached.put(dictionaryName + language + expkey + expkey2, result);
        }
        return result;
    }

    public String getDictionaryItem(String dictionaryName, String dictionaryKey, Date date, String ... languages) {
        List<Dictionary_data> items = cached.get(dictionaryName + dictionaryKey + java.util.Arrays.asList(languages));
        if (items == null) {
            items = getDictionary(dictionaryName, dictionaryKey, null, date, null, null);
            cached.put(dictionaryName + dictionaryKey + java.util.Arrays.asList(languages), items);
        }
        return filterByLanguage(items, languages);
    }

    public String getDictionaryItem(String dictionaryName, String dictionaryExp, String dictionaryKey, Date date, String ... languages) {
        List<Dictionary_data> items = cached.get(dictionaryName + dictionaryKey + dictionaryExp + java.util.Arrays.asList(languages));
        if (items == null) {
            items = getDictionary(dictionaryName, dictionaryKey, null, date, dictionaryExp, null);
            cached.put(dictionaryName + dictionaryKey + dictionaryExp + java.util.Arrays.asList(languages), items);
        }
        return filterByLanguage(items, languages);
    }

    public String getDictionaryItem(String dictionaryName, String dictionaryExp, String dictionaryExp2,
        String dictionaryKey, Date date, String ... languages) {
        List<Dictionary_data> items = cached.get(dictionaryName + dictionaryKey + dictionaryExp + dictionaryExp2 + java.util.Arrays.asList(languages));
        if (items == null) {
            items = getDictionary(dictionaryName, dictionaryKey, null, date, dictionaryExp, dictionaryExp2);
            cached.put(dictionaryName + dictionaryKey + dictionaryExp + dictionaryExp2 + java.util.Arrays.asList(languages), items);
        }
        return filterByLanguage(items, languages);
    }

    private List<Dictionary_data> getDictionary(String dictionaryName, String dictionaryKey,
        String language, Date date, String dictionaryExp, String dictionaryExp2) {
        EntityManager em = JpaRequestCycle.get().getEntityManager();
        Query queryBrand = em.createQuery("select x from Dictionary_data x where "
            + "x.name = :dictionaryName "
            + "and x.valid = true and x.fromdate <= :currdate and x.todate >= :currdate "
            + (language != null ? "and x.language = :language " : "")
            + (dictionaryKey != null ? "and x.dkey = :dkey " : "")
            + (dictionaryExp != null ? "and x.expkey = :expkey " : "")
            + (dictionaryExp2 != null ? "and x.expkey2 = :expkey2 " : "")
            + "order by x.dvalue");
        queryBrand.setParameter("dictionaryName", dictionaryName);
        queryBrand.setParameter("currdate", date);
        if (language != null) {
            queryBrand.setParameter("language", language);
        }
        if (dictionaryKey != null) {
            queryBrand.setParameter("dkey", dictionaryKey);
        }
        if (dictionaryExp != null) {
            queryBrand.setParameter("expkey", dictionaryExp);
        }
        if (dictionaryExp2 != null) {
            queryBrand.setParameter("expkey2", dictionaryExp2);
        }
        return queryBrand.getResultList();
    }

    private String filterByLanguage(List<Dictionary_data> items, String[] languages) {
        for (String language : languages) {
            for (Dictionary_data item : items) {
                if (language.equals(item.getLanguage())) {
                    return item.getDvalue();
                }
            }
        }
        return items.isEmpty() ? "" : items.get(0).getDvalue();
    }
}
