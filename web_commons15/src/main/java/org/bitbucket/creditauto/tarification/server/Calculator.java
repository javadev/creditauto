package org.bitbucket.creditauto.tarification.server;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TYPE_PER_CENT = "1";
    public static final String TYPE_PER_AMOUNT = "2";
    private static final BigDecimal H100 = BigDecimal.valueOf(100);

    public static BigDecimal calculateDownPayment(BigDecimal goodPrice,
            BigDecimal initPaymentValue, String initPaymentType) {

        BigDecimal result = null;
        if (goodPrice != null && initPaymentValue != null && TYPE_PER_AMOUNT.equals(initPaymentType)) {
            result = initPaymentValue.multiply(H100).divide(goodPrice, RoundingMode.CEILING);
        }
        if (goodPrice != null && initPaymentValue != null && TYPE_PER_CENT.equals(initPaymentType)) {
            result = initPaymentValue.multiply(goodPrice).divide(H100, RoundingMode.CEILING);
        }
        return result;
    }

}
