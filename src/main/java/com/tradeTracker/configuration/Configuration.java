package com.tradeTracker.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuration {

    @XmlElement
    private BrokerData brokerData;
    @XmlElement
    private EmailData emailData;

    public BrokerData getBrokerData() {
        return brokerData;
    }

    public EmailData getEmailData() {
        return emailData;
    }
}