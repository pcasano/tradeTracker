package com.tradeTracker.reportContents;

import org.w3c.dom.NamedNodeMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlexStatement {

    private String accountId;
    private Date fromDate;
    private Date toDate;
    private String period;
    private Date whenGenerated;

    public FlexStatement(NamedNodeMap namedNodeMap) throws ParseException {
        this.accountId = namedNodeMap.getNamedItem("accountId").getTextContent();
        this.period = namedNodeMap.getNamedItem("period").getTextContent();
        this.fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(namedNodeMap.getNamedItem("fromDate").getTextContent());
        this.toDate = new SimpleDateFormat("dd/MM/yyyy").parse(namedNodeMap.getNamedItem("toDate").getTextContent());
        this.whenGenerated = new SimpleDateFormat("dd/MM/yyyy;HH:mm:ss").parse(namedNodeMap.getNamedItem("whenGenerated").getTextContent());
    }

    public String getAccountId() {
        return accountId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getPeriod() {
        return period;
    }

    public Date getWhenGenerated() {
        return whenGenerated;
    }
}
