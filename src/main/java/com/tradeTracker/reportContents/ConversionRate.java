package com.tradeTracker.reportContents;

import org.w3c.dom.NamedNodeMap;

public class ConversionRate {

    private final String fromCurrency;
    private final String toCurrency;
    private final double rate;

    public ConversionRate(NamedNodeMap namedNodeMap) {
        this.fromCurrency = namedNodeMap.getNamedItem("fromCurrency").getTextContent();
        this.toCurrency = namedNodeMap.getNamedItem("toCurrency").getTextContent();
        this.rate = Double.parseDouble(namedNodeMap.getNamedItem("rate").getTextContent());
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getRate() {
        return rate;
    }
}
