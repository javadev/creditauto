/*
 * $Id$
 */
package org.bitbucket.creditauto.tarification.server.amortization;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.bitbucket.creditauto.tarification.server.AbstractTarificationTestCase;
import org.bitbucket.creditauto.tarification.server.model.CalculationInputParameters;
import org.bitbucket.creditauto.tarification.server.model.PaymentDate;
import org.junit.Test;

/**
 * CarCalculationTest.
 * @author vko
 * @version $Revision$ $Date$
 */
public class CarCalculationTest extends AbstractTarificationTestCase {
    private CarCalculation carCalculation;

    /**
     * Test method for calcAnnuitySums(double, int).
     */
    @Test
    public void testCalcAnnuitySums() {
        CalculationInputParameters params = createObjectFromData("testCalcAnnuitySums",
                CalculationInputParameters.class);
        carCalculation = new CarCalculation(Collections.<PaymentDate>emptyList(), params);
        double annuity = carCalculation.calcAnnuitySums(1000D, 10);
        assertEquals("Should be equal", 104.64D, annuity, 2);
    }

    /**
     * Test method for calcAnnuitySums(double, int).
     */
    @Test
    public void testCalcAnnuitySumsWithMonthlyFee() {
        CalculationInputParameters params = createObjectFromData("testCalcAnnuitySumsWithMonthlyFee",
                CalculationInputParameters.class);
        carCalculation = new CarCalculation(Collections.<PaymentDate>emptyList(), params);
        assertEquals("Should be equal", 104.64D, carCalculation.getAnnuitySum(), 2);
        assertEquals("Should be equal", 139.64D, carCalculation.getMonthlyInstallment(), 2);
    }

    /**
     * Test method for calcAnnuitySums(double, int).
     */
    @Test
    public void testCalcAnnuitySumsWithMonthlyFee2() {
        CalculationInputParameters params = createObjectFromData("testCalcAnnuitySumsWithMonthlyFee2",
                CalculationInputParameters.class);
        carCalculation = new CarCalculation(Collections.<PaymentDate>emptyList(), params);
        assertEquals("Should be equal", 1012.94D, carCalculation.getAnnuitySum(), 2);
        assertEquals("Should be equal", 1148.9D, carCalculation.getMonthlyInstallment(), 2);
    }
}
