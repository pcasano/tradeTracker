package com.tradeTracker.reportContents;

import org.w3c.dom.NamedNodeMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatementOfFundsLine {

    private final String currency;
    private final double fxRateToBase;
    private final String description;
    private final Date date;
    private final String activityCode;
    private final String activityDescription;
    private final double amount;
    private final double balance;
    private final String levelOfDetail;

    public StatementOfFundsLine(NamedNodeMap namedNodeMap) throws ParseException {
        this.currency = namedNodeMap.getNamedItem("currency").getTextContent();
        this.fxRateToBase = Double.parseDouble(namedNodeMap.getNamedItem("fxRateToBase").getTextContent());
        this.description = namedNodeMap.getNamedItem("description").getTextContent();
        this.date = new SimpleDateFormat("dd/MM/yyyy").parse(namedNodeMap.getNamedItem("date").getTextContent());
        this.activityCode = namedNodeMap.getNamedItem("activityCode").getTextContent();
        this.activityDescription = namedNodeMap.getNamedItem("activityDescription").getTextContent();
        this.amount = Double.parseDouble(namedNodeMap.getNamedItem("amount").getTextContent());
        this.balance = Double.parseDouble(namedNodeMap.getNamedItem("balance").getTextContent());
        this.levelOfDetail = namedNodeMap.getNamedItem("levelOfDetail").getTextContent();
    }

    public String getCurrency() {
        return currency;
    }

    public double getFxRateToBase() {
        return fxRateToBase;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getLevelOfDetail() {
        return levelOfDetail;
    }
}
