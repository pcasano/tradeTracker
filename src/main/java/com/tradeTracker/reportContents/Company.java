package com.tradeTracker.reportContents;

import java.util.Date;

public class Company {

    private final Date paymentDate;
    private final String companyName;
    private final double amount;
    private final double tax;
    private final String currency;
    private final double rate;


    public Company(StatementOfFundsLine dividendEntry, StatementOfFundsLine taxEntry, double rate) {
        this.amount = dividendEntry.getAmount();
        this.companyName = dividendEntry.getDescription();
        this.tax = taxEntry.getAmount();
        this.currency = dividendEntry.getCurrency();
        this.paymentDate = dividendEntry.getDate();
        this.rate = rate;
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
