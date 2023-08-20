package org.bitbucket.creditauto.dictionary.facade;

import java.util.Date;
import java.util.List;
import org.bitbucket.creditauto.entity.Dictionary_data;

public interface IDictionary {
    /** Ukrainian */
    String LANGUAGE_UK = "uk";

    /** Russian */
    String LANGUAGE_RU = "ru";

    /**
     * Gets list of Dictionary_date objects.
     *
     * @param dictionaryName - the name of dictionary
     * @param language - the language key (ru/uk)
     * @param date - the date
     * @return the list of objects
     */
    List<Dictionary_data> getDictionary(String dictionaryName, String language, Date date);

    /**
     * Gets dictionary item.
     *
     * @param dictionaryName - the name of dictionary
     * @param dictionaryKey - the key of dictionary
     * @param date - the date
     * @param languages - the language keys (ru/uk)
     * @return the list of objects
     */
    String getDictionaryItem(
            String dictionaryName, String dictionaryKey, Date date, String... languages);

    /**
     * Gets dictionary item.
     *
     * @param dictionaryName - the name of dictionary
     * @param dictionaryExp - the exp name
     * @param dictionaryKey - the key of dictionary
     * @param date - the date
     * @param languages - the language keys (ru/uk)
     * @return the list of objects
     */
    String getDictionaryItem(
            String dictionaryName,
            String dictionaryExp,
            String dictionaryKey,
            Date date,
            String... languages);

    /**
     * Gets dictionary item.
     *
     * @param dictionaryName - the name of dictionary
     * @param dictionaryExp - the exp name
     * @param dictionaryExp2 - the exp2 name
     * @param dictionaryKey - the key of dictionary
     * @param date - the date
     * @param languages - the language keys (ru/uk)
     * @return the list of objects
     */
    String getDictionaryItem(
            String dictionaryName,
            String dictionaryExp,
            String dictionaryExp2,
            String dictionaryKey,
            Date date,
            String... languages);

    /**
     * Gets list of Dictionary_date objects.
     *
     * @param dictionaryName - the name of dictionary
     * @param expkey - the expkey parameter
     * @param language - the language key (ru/uk)
     * @param date - the date
     * @return the list of objects
     */
    List<Dictionary_data> getDictionary(
            String dictionaryName, String expKey, String language, Date date);

    /**
     * Gets list of Dictionary_date objects.
     *
     * @param dictionaryName - the name of dictionary
     * @param expkey - the expkey parameter
     * @param expkey2 - the expkey2 parameter
     * @param language - the language key (ru/uk)
     * @param date - the date
     * @return the list of objects
     */
    List<Dictionary_data> getDictionary(
            String dictionaryName, String expKey, String expKey2, String language, Date date);
}
