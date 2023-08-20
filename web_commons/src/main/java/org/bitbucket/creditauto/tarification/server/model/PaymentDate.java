/*
 * $Id$
 */
package org.bitbucket.creditauto.tarification.server.model;

import java.io.Serializable;
import java.util.Date;

/**
 * .
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class PaymentDate implements Serializable {

    private static final long serialVersionUID = -9078389073349605262L;
    private Date date;
    private long daysBefore;
    private long daysPerMonth;

    /** Default constructor. */
    public PaymentDate() {
        daysBefore = 0;
    }

    /**
     * Constructor.
     *
     * @param date - payment date
     * @param daysBefore - number of days from previous date to this one
     */
    public PaymentDate(Date date, long daysBefore) {
        this.date = date;
        this.daysBefore = daysBefore;
    }

    /**
     * Get payment date.
     *
     * @return payment date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set payment date.
     *
     * @param date - payment date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get number of days from previous payment date to this one.
     *
     * @return number of days from previous payment date to this one
     */
    public long getDaysBefore() {
        return daysBefore;
    }

    /**
     * Set number of days from previous payment date to this one.
     *
     * @param daysBefore - number of days from previous payment date to this one
     */
    public void setDaysBefore(long daysBefore) {
        this.daysBefore = daysBefore;
    }

    /**
     * Get number of days in month is defined by date.
     *
     * @return number of days in moth
     */
    public long getDaysPerMonth() {
        return daysPerMonth;
    }

    /**
     * Set number of days in month is defined by date.
     *
     * @param daysPerMonth - number of days in moth
     */
    public void setDaysPerMonth(long daysPerMonth) {
        this.daysPerMonth = daysPerMonth;
    }
}
