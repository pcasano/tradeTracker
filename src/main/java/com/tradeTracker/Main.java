package com.tradeTracker;

import com.tradeTracker.email.DividendMessageBuilder;
import com.tradeTracker.email.MessageBuilder;
import com.tradeTracker.reportContents.Company;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, SAXException, InterruptedException, ParseException {

        Configuration configuration = new ConfigurationReader().unmarshal();

        RestAssured.baseURI = configuration.getBrokerData().getBaseUrl();
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("t", configuration.getBrokerData().getToken())
                .queryParam("q", configuration.getBrokerData().getQueryId())
                .queryParam("v", configuration.getBrokerData().getApiVersion())
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
                .queryParam("t", configuration.getBrokerData().getToken())
                .queryParam("v", configuration.getBrokerData().getApiVersion())
                .get("FlexStatementService.GetStatement");

        String xmlStringForContent = responseForContent.asString();


        XmlParser xmlParserForContent = new XmlParser(xmlStringForContent);
        List<StatementOfFundsLine> listOfStatementOfFundsLine = xmlParserForContent.getListOfStatementOfFundsLine();


        List<StatementOfFundsLine> listOfDivs = listOfStatementOfFundsLine.stream()
                .filter(entry -> entry.getLevelOfDetail().equals("BaseCurrency") && entry.getActivityCode().equals("DIV")).toList();

        List<StatementOfFundsLine> listOfTaxes = listOfStatementOfFundsLine.stream()
                .filter(entry -> entry.getLevelOfDetail().equals("BaseCurrency") && entry.getActivityCode().equals("FRTAX")).toList();
        List<Company> listOfCompanies = new ArrayList<>();

        listOfDivs.forEach(dividend -> {
            listOfCompanies.add(new Company(
                    dividend,
                    listOfTaxes.stream().filter(tax -> tax.getDescription().equals(dividend.getDescription())).findFirst().get())
        );
        });


        FlexStatement flexStatement = xmlParserForContent.getFlexStatement();

        new DividendMessageBuilder(new ArrayList<Company>(), configuration).sendDividendEmail();
    }

}