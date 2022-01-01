package com.tradeTracker.configuration;

import javax.xml.bind.annotation.XmlElement;

public class BrokerData {

    @XmlElement
    private String token;
    @XmlElement
    private String dailyQueryId;
    @XmlElement
    private String monthlyQueryId;
    @XmlElement
    private String lastMonthQueryId;
    @XmlElement
    private String baseUrl;
    @XmlElement
    private String apiVersion;

    public String getToken() {
        return token;
    }

    public String getDailyQueryId() {
        return dailyQueryId;
    }

    public String getMonthlyQueryId() {
        return monthlyQueryId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getLastMonthQueryId() {
        return lastMonthQueryId;
    }
}
