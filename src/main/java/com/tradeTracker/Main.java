package com.tradeTracker;

import com.tradeTracker.email.DividendMessageBuilder;
import com.tradeTracker.email.TradeMessageBuilder;
import com.tradeTracker.reportContents.Company;
import com.tradeTracker.reportContents.FlexStatement;
import com.tradeTracker.reportContents.StatementOfFundsLine;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.configuration.ConfigurationReader;
import java.util.Comparator;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

        List<StatementOfFundsLine> listOfDivsBase = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "BaseCurrency", "DIV");
        List<StatementOfFundsLine> listOfTaxesBase = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "BaseCurrency", "FRTAX");
        List<Company> listOfDivCompaniesBase = getListOfDivCompanies(listOfDivsBase, listOfTaxesBase);

        List<StatementOfFundsLine> listOfTradesBase = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "BaseCurrency", "BUY", "SELL");
        List<Company> listOfTradeCompaniesBase = getListOfTradeCompanies(listOfTradesBase);

        List<StatementOfFundsLine> listOfDivs = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "Currency", "DIV");
        List<StatementOfFundsLine> listOfTaxes = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "Currency", "FRTAX");
        List<Company> listOfDivCompanies = getListOfDivCompanies(listOfDivs, listOfTaxes);

        List<StatementOfFundsLine> listOfTrades = getListOfStatementOfFundsLine(listOfStatementOfFundsLine, "Currency", "BUY", "SELL");
        List<Company> listOfTradeCompanies = getListOfTradeCompanies(listOfTrades);

        new DividendMessageBuilder(listOfDivCompaniesBase, listOfDivCompanies, configuration).sendDividendEmail();
        new TradeMessageBuilder(listOfTradeCompaniesBase, listOfTradeCompanies, configuration).sendTradeEmail();

        FlexStatement flexStatement = xmlParserForContent.getFlexStatement();

    }

    private static List<Company> getListOfDivCompanies(
            List<StatementOfFundsLine> listOfDivs,
            List<StatementOfFundsLine> listOfTaxes
    ) throws ParseException {
        List<Company> listOfCompanies = new ArrayList<>();

        for (StatementOfFundsLine dividend : listOfDivs) {
            listOfCompanies.add(new Company(
                    dividend,
                    listOfTaxes.stream().filter(tax -> tax.getDescription().equals(dividend.getDescription())).findFirst()
            ));
        }
        listOfCompanies.sort(Comparator.comparing(Company::getPaymentDate));
        return listOfCompanies;
    }

    private static List<Company> getListOfTradeCompanies(
            List<StatementOfFundsLine> listOfTrades
    ) throws ParseException {
        List<Company> listOfCompanies = new ArrayList<>();

        for (StatementOfFundsLine dividend : listOfTrades) {
            listOfCompanies.add(new Company(
                    dividend,
                    Optional.empty()
            ));
        }
        listOfCompanies.sort(Comparator.comparing(Company::getPaymentDate));
        return listOfCompanies;
    }

    private static List<StatementOfFundsLine> getListOfStatementOfFundsLine(
            List<StatementOfFundsLine> listOfStatementOfFundsLine,
            String levelOfDetail,
            String... activityCode) {
        return listOfStatementOfFundsLine.stream()
                .filter(entry -> entry.getLevelOfDetail().equals(levelOfDetail) && Arrays.asList(activityCode).contains(entry.getActivityCode())).toList();
    }
}