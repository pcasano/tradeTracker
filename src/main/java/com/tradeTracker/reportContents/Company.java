package com.tradeTracker.reportContents;

import java.util.Date;
import java.util.Optional;

public class Company {

    private final Date paymentDate;
    private final String companyName;
    private final double amount;
    private double tax;
    private final String currency;
    private final double rate;
    private final String activityCode;


    public Company(StatementOfFundsLine dividendEntry, Optional<StatementOfFundsLine> optionalTaxEntry) {
        this.amount = dividendEntry.getAmount();
        this.companyName = dividendEntry.getDescription();
        optionalTaxEntry.ifPresent(taxEntry -> this.tax = taxEntry.getAmount());
        this.currency = dividendEntry.getCurrency();
        this.paymentDate = dividendEntry.getDate();
        this.rate = dividendEntry.getFxRateToBase();
        this.activityCode = dividendEntry.getActivityCode();

    }

    public Company(Date paymentDate, String companyName, double amount, String currency, double rate, String activityCode, double tax) {
        this.paymentDate = paymentDate;
        this.companyName = companyName;
        this.amount = amount;
        this.currency = currency;
        this.rate = rate;
        this.activityCode = activityCode;
        this.tax = tax;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getAmount() {
        return amount;
    }

    public double getTax() {
        return tax;
    }

    public String getCurrency() {
        return currency;
    }

    public double getRate() {
        return rate;
    }
}
