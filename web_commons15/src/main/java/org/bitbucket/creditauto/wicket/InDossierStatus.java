package org.bitbucket.creditauto.wicket;

import java.util.Date;

import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.dictionary.server.DictionaryServerImpl;
import org.bitbucket.creditauto.entity.Dictionary_data;

public enum InDossierStatus {
    /** Неопределенный */
    UNDEFINED("0"),
    /** Сделано фин. предложение */
    SIMULATION("10"),
    /** Сделка прервана */
    INTERRUPTED("20"),
    /** Клиент подписал анкету-заявление */
    APPLICATIONFORM_SINED("30"),
    /** Сделка ожидает решения */
    TOBE_STUDY("40"),
    /** Сделка одобрена экспертной системой */
    ACCEPTED_ES("50"),
    /** Сделка ожидает финального решения перед утверждением */
    WAITING_FOR_FINAL_DECISION_BEFORE_ACCEPT("60"),
    /** Сделка одобрена контактным центром */
    ACCEPTED_CC("70"),
    /** Сделка ожидает создания договора в банковской системе */
    TOBE_CONTRACT_SIGNED("80"),
    /** Клиент подписал контракт */
    CONTRACT_SIGNED("90"),
    /** Сделка ожидает финансирования */
    TOBE_FINANCED("100"),
    /** Сделка профинансирована в банковской системе */
    FINANCED("110"),
    /** Отказ по решению Экспертной Системы */
    REFUSAL_ES("120"),
    /** Сделка ожидает финального решения перед отказом */
    WAITING_FOR_FINAL_DECISION_BEFORE_REFUSAL("130"),
    /** Отказ по решению Контактного Центра */
    REFUSAL_CC("140"),
    /** Сделка ожидает финального решения перед отменой */
    WAITING_FOR_FINAL_DECISION_BEFORE_CANCEL("150"),
    /** Отмена сделки по решению контактного центра */
    CANCEL_CC("160"),
    /** Предварительная отмена сделки перед возвратом товара */
    PRE_GOOD_RETURN("170"),
    /** Возврат товара */
    GOOD_RETURN("180");
    private String key;

    InDossierStatus(String key) {
        this.key = key;
    }
    public String getStatusName() {
        for (Dictionary_data item : new DictionaryServerImpl().getDictionary("statusList", IDictionary.LANGUAGE_RU, new Date())) {
            if (this.key.equalsIgnoreCase(item.getDkey())) {
                return item.getDvalue();
            }
        }
        return null;
    }
    public String getKey() {
        return this.key;
    }

    public static InDossierStatus statusByKey(String dKey) {
        for (InDossierStatus status : values()) {
            if (status.getKey().equalsIgnoreCase(dKey)) {
                return status;
            }
        }
        return null;
    }
    public static String getStatusNameByKey(String dKey) {
        for (Dictionary_data item : new DictionaryServerImpl().getDictionary("statusList",
                IDictionary.LANGUAGE_RU, new Date())) {
            if (item.getDkey().equalsIgnoreCase(dKey)) {
                return item.getDvalue();
            }
        }
        return null;
    }

}
