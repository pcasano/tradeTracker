package com.tradeTracker.configuration;

import javax.xml.bind.annotation.XmlElement;

public class BrokerData {

    @XmlElement
    private String token;
    @XmlElement
    private String queryId;
    @XmlElement
    private String baseUrl;
    @XmlElement
    private String apiVersion;

    public String getToken() {
        return token;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
