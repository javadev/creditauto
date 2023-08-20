/*
 * $Id$
 */
package org.bitbucket.creditauto.tarification.server.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CalculationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Payment payment;
    private int repaymentDay;
    private Long endGracePeriod;
    private BigDecimal externalDistributorFinancingAmount;
    private BigDecimal amountOfLoan;
    private Date contractFinancedDate;
    private Date contractFinancedDateToPrint;
    private Date nextWorkDate;
    private Date firstPaymentDate;
    private Date lastPaymentDate;
    private Date endGracePeriodDate;
    private List<Payment> payments;

    /**
     * Set total payment information.
     *
     * @param payment - total payments
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
        if (null == payment) {
            return;
        }
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setRepaymentDay(int repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public int getRepaymentDay() {
        return repaymentDay;
    }

    public Date getContractFinancedDate() {
        return contractFinancedDate;
    }

    public void setContractFinancedDate(Date contractFinancedDate) {
        this.contractFinancedDate = contractFinancedDate;
    }

    public Date getContractFinancedDateToPrint() {
        return contractFinancedDateToPrint;
    }

    public void setContractFinancedDateToPrint(Date contractFinancedDateToPrint) {
        this.contractFinancedDateToPrint = contractFinancedDateToPrint;
    }

    public Date getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(Date firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public Date getEndGracePeriodDate() {
        return endGracePeriodDate;
    }

    public void setEndGracePeriodDate(Date endGracePeriodDate) {
        this.endGracePeriodDate = endGracePeriodDate;
    }

    public void setNextWorkDate(Date nextWorkDate) {
        this.nextWorkDate = nextWorkDate;
    }

    public Date getNextWorkDate() {
        return nextWorkDate;
    }

    public double getOpenFeeRate() {
        return null == payment ? 0 : payment.getOpeningFeeRate();
    }

    public double getOpenFeePayment() {
        return null == payment ? 0 : payment.getOpeningFee();
    }

    public double getMonthlyFeeRate() {
        return null == payment ? 0 : payment.getMonthlyFeeRate();
    }

    public double getMonthlyFeePayment() {
        return null == payment ? 0 : payment.getMonthlyFeePayment();
    }

    public double getMonthlyPayment() {
        return null == payment ? 0 : payment.getTotalInstalmentPayment();
    }

    public double getRate() {
        return null == payment ? 0 : payment.getNominalRate();
    }

    public void setExternalDistributorFinancingAmount(
            BigDecimal externalDistributorFinancingAmount) {
        this.externalDistributorFinancingAmount = externalDistributorFinancingAmount;
    }

    public BigDecimal getExternalDistributorFinancingAmount() {
        return externalDistributorFinancingAmount;
    }

    public BigDecimal getAmountOfLoan() {
        return amountOfLoan;
    }

    public void setAmountOfLoan(BigDecimal amountOfLoan) {
        this.amountOfLoan = amountOfLoan;
    }

    public void setEndGracePeriod(Long endGracePeriod) {
        this.endGracePeriod = endGracePeriod;
    }

    public Long getEndGracePeriod() {
        return endGracePeriod;
    }

    public String toString() {
        return "TarificationCalulateResult [amountOfLoan=" + amountOfLoan + "]";
    }
}
