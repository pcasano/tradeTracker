package com.tradeTracker;

import com.tradeTracker.reportContents.FlexStatement;
import com.tradeTracker.reportContents.StatementOfFundsLine;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.configuration.ConfigurationReader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, SAXException, InterruptedException, ParseException {

        Configuration configuration = new ConfigurationReader().unmarshal();

        RestAssured.baseURI = configuration.getBaseUrl();
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("t", configuration.getToken())
                .queryParam("q", configuration.getQueryId())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.SendRequest");
        String xmlStringForReferenceCode = response.asString();
        XmlParser xmlParserForReferenceCode = new XmlParser(xmlStringForReferenceCode);

        if(!xmlParserForReferenceCode.getStatus().equals("Success")) {
            if(xmlParserForReferenceCode.getErrorCode().equals("1020")){
                // todo: set logs
                throw new AccessDeniedException("Access denied");
            }
        }
        String referenceCode = xmlParserForReferenceCode.getReferenceCode();

        TimeUnit.SECONDS.sleep(2);
        RequestSpecification requestForContent = RestAssured.given();
        Response responseForContent = requestForContent.queryParam("q", referenceCode)
                .queryParam("t", configuration.getToken())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.GetStatement");

        String xmlStringForContent = responseForContent.asString();
        XmlParser xmlParserForContent = new XmlParser(xmlStringForContent);
        List<StatementOfFundsLine> listOfStatementOfFundsLine = xmlParserForContent.getListOfStatementOfFundsLine();
        List<StatementOfFundsLine> listOfStatementOfFundsLineBaseCurrency =
                listOfStatementOfFundsLine.stream().filter(entry -> entry.getLevelOfDetail().equals("BaseCurrency")).collect(Collectors.toList());
        List<StatementOfFundsLine> listOfStatementOfFundsLineCurrency =
                listOfStatementOfFundsLine.stream().filter(entry -> entry.getLevelOfDetail().equals("Currency")).collect(Collectors.toList());
        FlexStatement flexStatement = xmlParserForContent.getFlexStatement();

        System.out.println("");
    }

}