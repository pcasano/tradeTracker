package com.tradeTracker.email;

import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.reportContents.Company;

import java.util.List;

public class DividendMessageBuilder extends MessageBuilder{

    private final List<Company> listOfCompanies;

    public DividendMessageBuilder(List<Company> listOfCompanies, Configuration configuration) {
        super(configuration);
        this.listOfCompanies = listOfCompanies;
    }

    public void sendDividendEmail() {
        this.sendEmail();
    }
}
