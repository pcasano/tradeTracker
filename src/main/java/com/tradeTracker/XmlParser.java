package com.tradeTracker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.tradeTracker.reportContents.ConversionRate;
import com.tradeTracker.reportContents.FlexStatement;
import com.tradeTracker.reportContents.StatementOfFundsLine;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    private final Document document;

    public XmlParser(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        this.document = builder.parse(is);
        this.document.getDocumentElement().normalize();

    }

    public String getReferenceCode() {
        return this.document.getElementsByTagName("ReferenceCode").item(0).getTextContent();
    }

    public String getStatus() {
        return this.document.getElementsByTagName("Status").item(0).getTextContent();
    }

    public String getErrorMessage() {
        return this.document.getElementsByTagName("ErrorMessage").item(0).getTextContent();
    }

    public String getErrorCode() {
        return this.document.getElementsByTagName("ErrorCode").item(0).getTextContent();
    }

    public FlexStatement getFlexStatement() throws ParseException {
        return new FlexStatement(this.document.getElementsByTagName("FlexStatement").item(0).getAttributes());
    }

    public List<StatementOfFundsLine> getListOfStatementOfFundsLine() throws ParseException {
        List<StatementOfFundsLine> listStatementOfFundsLine = new ArrayList<>();
        NodeList nodeList = this.document.getElementsByTagName("StmtFunds").item(0).getChildNodes();
        for (int index=0; index<nodeList.getLength(); index++) {
            if (nodeList.item(index) instanceof Element) {
                listStatementOfFundsLine.add(new StatementOfFundsLine(nodeList.item(index).getAttributes()));
            }
        }
        return listStatementOfFundsLine;
    }
}
