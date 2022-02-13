package com.tradeTracker;

import com.tradeTracker.configuration.Query;
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
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("-------------------- Application starts --------------------");
        Configuration configuration = new ConfigurationReader().unmarshal();

        RestAssured.baseURI = configuration.getBrokerData().getBaseUrl();
        RequestSpecification request = RestAssured.given();
        logger.info( "connection to IB");
        Response response = request.queryParam("t", configuration.getBrokerData().getToken())
                .queryParam("q", new Query(configuration, args).getQueryId())
                .queryParam("v", configuration.getBrokerData().getApiVersion())
                .get("FlexStatementService.SendRequest");
        String xmlStringForReferenceCode = response.asString();
        XmlParser xmlParserForReferenceCode = new XmlParser(xmlStringForReferenceCode);

        if(!xmlParserForReferenceCode.getStatus().equals("Success")) {
            if(xmlParserForReferenceCode.getErrorCode().equals("1020")) {
                throw new AccessDeniedException("connection to IB failed: Access denied");
            } else {
                throw new Exception("Statement could not be generated at this time");
            }
        }
        String referenceCode = xmlParserForReferenceCode.getReferenceCode();

        TimeUnit.SECONDS.sleep(2);
        RequestSpecification requestForContent = RestAssured.given();
        logger.info( "retrieving data from IB");
        Response responseForContent = requestForContent.queryParam("q", referenceCode)
                .queryParam("t", configuration.getBrokerData().getToken())
                .queryParam("v", configuration.getBrokerData().getApiVersion())
                .get("FlexStatementService.GetStatement");

        String xmlStringForContent = responseForContent.asString();

        logger.info( "parsing data from IB response");
        XmlParser xmlParserForContent = new XmlParser(xmlStringForContent);
        FlexStatement flexStatement = xmlParserForContent.getFlexStatement();
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

        if(!listOfDivCompanies.isEmpty()) {
            new DividendMessageBuilder(listOfDivCompaniesBase, listOfDivCompanies, configuration, flexStatement).sendDividendEmail();
        } else {
            logger.info("No dividends payments found");
        }
        if(!listOfTradeCompanies.isEmpty()) {
            new TradeMessageBuilder(listOfTradeCompaniesBase, listOfTradeCompanies, configuration, flexStatement).sendTradeEmail();
        } else {
            logger.info("No trades found");
        }
        logger.info("--------------------- Application ends ---------------------");
    }

    private static List<Company> getListOfTradeCompanies(List<StatementOfFundsLine> listOfTrades) {
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
    
    private static List<Company> getListOfDivCompanies(List<StatementOfFundsLine> listOfDivs, List<StatementOfFundsLine> listOfTaxes) {
        ArrayList<Company> listOfCompanies = new ArrayList<>();
        Map<String, Double> companyDivsMap = listOfDivs.stream()
                .collect(
                        Collectors.groupingBy(
                                StatementOfFundsLine::getDescription,
                                Collectors.summingDouble(StatementOfFundsLine::getAmount)));
        Map<String, Double> companyTaxesMap = listOfTaxes.stream()
                .collect(
                        Collectors.groupingBy(
                                StatementOfFundsLine::getDescription,
                                Collectors.summingDouble(StatementOfFundsLine::getAmount)));

        companyDivsMap.forEach((key, value) -> {
            StatementOfFundsLine targetCompany = listOfDivs.stream()
                    .filter(statementOfFundsLine -> statementOfFundsLine.getDescription().equals(key)).findFirst().get();
            listOfCompanies.add(new Company(
                    targetCompany.getDate(),
                    targetCompany.getDescription(),
                    value,
                    targetCompany.getCurrency(),
                    targetCompany.getFxRateToBase(),
                    targetCompany.getActivityCode(),
                    Optional.ofNullable(companyTaxesMap.get(targetCompany.getDescription()))));
        });
        listOfCompanies.sort(Comparator.comparing(Company::getPaymentDate));
        return listOfCompanies;
    }
}