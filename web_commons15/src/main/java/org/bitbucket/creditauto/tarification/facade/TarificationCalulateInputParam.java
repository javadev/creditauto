/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.tarification.facade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bitbucket.creditauto.entity.Credittype;

public class TarificationCalulateInputParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private double amount;
    private double goodPrice;
    private double totalPrice;
    private double downPayment;
    private double desiredInstallment;
    private double currentInstallment;
    private long dossierId;
    private long duration;
    private long externalDistributorId;
    private int downPaymentStep;
    private int repaymentDay;
    private Date startDate;
    private String typeOfFirstDueDateCalc;
    private String user;
    private Credittype creditType;
    
    public TarificationCalulateInputParam() {
    }

    public TarificationCalulateInputParam(double amount,double goodPrice,long duration,long externalDistributorId) {
        init(amount, goodPrice, duration, externalDistributorId);
    }
    
    public void init(double amount, double goodPrice, long duration, long externalDistributorId) {
        this.amount = amount;
        this.goodPrice = goodPrice;
        this.duration = duration;
        this.externalDistributorId = externalDistributorId;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getGoodPrice() {
        return goodPrice;
    }
    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public long getExternalDistributorId() {
        return externalDistributorId;
    }
    public void setExternalDistributorId(long externalDistributorId) {
        this.externalDistributorId = externalDistributorId;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Credittype getCreditType() {
        return creditType;
    }
    public void setCreditType(Credittype creditType) {
        this.creditType = creditType;
    }
    public void setTypeOfFirstDueDateCalc(String typeOfFirstDueDateCalc) {
        this.typeOfFirstDueDateCalc = typeOfFirstDueDateCalc;
    }
    public String getTypeOfFirstDueDateCalc() {
        return typeOfFirstDueDateCalc;
    }
    public void setRepaymentDay(int repaymentDate) {
        this.repaymentDay = repaymentDate;
    }
    public int getRepaymentDay() {
        return repaymentDay;
    }
    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
    }
    public long getDossierId() {
        return dossierId;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }
    public double getDownPayment() {
        return downPayment;
    }
    public void setDesiredInstallment(double desiredInstallment) {
        this.desiredInstallment = desiredInstallment;
    }
    public double getDesiredInstallment() {
        return desiredInstallment;
    }
    public void setCurrentInstallment(double currentInstallment) {
        this.currentInstallment = currentInstallment;
    }
    public double getCurrentInstallment() {
        return currentInstallment;
    }
    public void setDownPaymentStep(int downPaymentStep) {
        this.downPaymentStep = downPaymentStep;
    }
    public int getDownPaymentStep() {
        return downPaymentStep;
    }
    @Override
    public String toString() {
        return "TarificationCalulateInputParam [amount=" + amount
                + ", creditTypeId=" + (creditType==null? "": String.valueOf(creditType.getId())) + "]";
    }
}
