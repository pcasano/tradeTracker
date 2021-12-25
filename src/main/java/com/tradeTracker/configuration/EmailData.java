package com.tradeTracker.configuration;

import javax.xml.bind.annotation.XmlElement;

public class EmailData {

    @XmlElement
    private String emailSender;
    @XmlElement
    private String emailSenderPass;
    @XmlElement
    private String targetEmail;

    public String getEmailSender() {
        return emailSender;
    }

    public String getEmailSenderPass() {
        return emailSenderPass;
    }

    public String getTargetEmail() {
        return targetEmail;
    }
}
