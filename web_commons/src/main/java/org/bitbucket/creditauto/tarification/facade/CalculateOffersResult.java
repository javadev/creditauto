/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.tarification.facade;

import java.util.ArrayList;
import java.util.List;
import org.bitbucket.creditauto.helpers.GeneralResult;
import org.bitbucket.creditauto.tarification.server.model.CalculationResult;

/**
 * .
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class CalculateOffersResult extends GeneralResult {
    private static final long serialVersionUID = 500L;
    private List<CalculationResult> calculationResults = new ArrayList<CalculationResult>();

    public void setCalculationResults(List<CalculationResult> calculationResults) {
        this.calculationResults = calculationResults;
    }

    public List<CalculationResult> getCalculationResults() {
        return calculationResults;
    }
}
