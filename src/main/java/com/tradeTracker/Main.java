package com.tradeTracker;

import com.tradeTracker.email.DividendMessageBuilder;
import com.tradeTracker.email.MessageBuilder;
import com.tradeTracker.reportContents.Company;
import com.tradeTracker.reportContents.ConversionRate;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, SAXException, InterruptedException, ParseException {

        Configuration configuration = new ConfigurationReader().unmarshal();
/*        new DividendMessageBuilder(new ArrayList<Company>(), configuration).sendDividendEmail();

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

        String xmlStringForContent = responseForContent.asString();*/
        String xmlStringForContent = "<FlexQueryResponse queryName=\"Dividendos (Clone)\" type=\"AF\">\n" +
                "\t<FlexStatements count=\"1\">\n" +
                "\t\t<FlexStatement accountId=\"U11639598\" fromDate=\"23/12/2021\" toDate=\"23/12/2021\" period=\"LastBusinessDay\" whenGenerated=\"24/12/2021;09:14:25\">\n" +
                "\t\t\t<StmtFunds>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA\" date=\"22/12/2021\" activityCode=\"DIV\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share (Ordinary Dividend)\" amount=\"32.2\" balance=\"475.196424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA\" date=\"22/12/2021\" activityCode=\"FRTAX\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share - ES Tax\" amount=\"-6.12\" balance=\"469.076424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"USD\" fxRateToBase=\"1\" description=\"VISCOFAN SA_\" date=\"22/12/2021\" activityCode=\"DIV\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share (Ordinary Dividend)\" amount=\"34.2\" balance=\"475.196424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA_\" date=\"22/12/2021\" activityCode=\"FRTAX\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share - ES Tax\" amount=\"-6.12\" balance=\"469.076424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"GBP\" fxRateToBase=\"1\" description=\"VISCOFAN SA__\" date=\"22/12/2021\" activityCode=\"DIV\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share (Ordinary Dividend)\" amount=\"36.2\" balance=\"475.196424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA__\" date=\"22/12/2021\" activityCode=\"FRTAX\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share - ES Tax\" amount=\"-6.12\" balance=\"469.076424917\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"\" date=\"23/12/2021\" activityCode=\"ADJ\" activityDescription=\"FX Translations P&amp;L\" amount=\"0.18786567\" balance=\"469.264290587\" levelOfDetail=\"BaseCurrency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA\" date=\"22/12/2021\" activityCode=\"DIV\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share (Ordinary Dividend)\" amount=\"32.2\" balance=\"56.62898415\" levelOfDetail=\"Currency\"/>\n" +
                "\t\t\t\t<StatementOfFundsLine currency=\"EUR\" fxRateToBase=\"1\" description=\"VISCOFAN SA\" date=\"22/12/2021\" activityCode=\"FRTAX\" activityDescription=\"VIS(ES0184262212) Cash Dividend EUR 1.40 per Share - ES Tax\" amount=\"-6.12\" balance=\"50.50898415\" levelOfDetail=\"Currency\"/>\n" +
                "\t\t\t</StmtFunds>\n" +
                "\t\t\t<Trades>\n" +
                "\t\t\t</Trades>\n" +
                "\t\t\t<ConversionRates>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"CHF\" toCurrency=\"EUR\" rate=\"0.96133\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"MXN\" toCurrency=\"EUR\" rate=\"0.042757\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"ZAR\" toCurrency=\"EUR\" rate=\"0.056363\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"INR\" toCurrency=\"EUR\" rate=\"0.011765\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"CNY\" toCurrency=\"EUR\" rate=\"0.13858\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"THB\" toCurrency=\"EUR\" rate=\"0.026345\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"AUD\" toCurrency=\"EUR\" rate=\"0.63954\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"KRW\" toCurrency=\"EUR\" rate=\"0.00074454\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"ILS\" toCurrency=\"EUR\" rate=\"0.28004\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"JPY\" toCurrency=\"EUR\" rate=\"0.0077165\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"PLN\" toCurrency=\"EUR\" rate=\"0.21588\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"GBP\" toCurrency=\"EUR\" rate=\"1.1838\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"HUF\" toCurrency=\"EUR\" rate=\"0.0027022\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"TRY\" toCurrency=\"EUR\" rate=\"0.077433\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"RUB\" toCurrency=\"EUR\" rate=\"0.012024\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"HKD\" toCurrency=\"EUR\" rate=\"0.1132\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"DKK\" toCurrency=\"EUR\" rate=\"0.13448\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"CAD\" toCurrency=\"EUR\" rate=\"0.68937\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"USD\" toCurrency=\"EUR\" rate=\"0.88281\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"NOK\" toCurrency=\"EUR\" rate=\"0.10009\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"SGD\" toCurrency=\"EUR\" rate=\"0.64974\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"CZK\" toCurrency=\"EUR\" rate=\"0.03991\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"SEK\" toCurrency=\"EUR\" rate=\"0.09714\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"NZD\" toCurrency=\"EUR\" rate=\"0.60259\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"CNH\" toCurrency=\"EUR\" rate=\"0.13848\"/>\n" +
                "\t\t\t\t<ConversionRate reportDate=\"23/12/2021\" fromCurrency=\"BRL\" toCurrency=\"EUR\" rate=\"0.15555\"/>\n" +
                "\t\t\t</ConversionRates>\n" +
                "\t\t</FlexStatement>\n" +
                "\t</FlexStatements>\n" +
                "</FlexQueryResponse>";

        XmlParser xmlParserForContent = new XmlParser(xmlStringForContent);
        List<StatementOfFundsLine> listOfStatementOfFundsLine = xmlParserForContent.getListOfStatementOfFundsLine();

        List<StatementOfFundsLine> listOfDivs = listOfStatementOfFundsLine.stream()
                .filter(entry -> entry.getLevelOfDetail().equals("BaseCurrency") && entry.getActivityCode().equals("DIV")).toList();

        List<StatementOfFundsLine> listOfTaxes = listOfStatementOfFundsLine.stream()
                .filter(entry -> entry.getLevelOfDetail().equals("BaseCurrency") && entry.getActivityCode().equals("FRTAX")).toList();
        List<Company> listOfCompanies = new ArrayList<>();

        for (StatementOfFundsLine dividend : listOfDivs) {
            listOfCompanies.add(new Company(
                    dividend,
                    listOfTaxes.stream().filter(tax -> tax.getDescription().equals(dividend.getDescription())).findFirst().get(),
                    getRatioGivenCurrency(xmlParserForContent, dividend.getCurrency())
            ));
        }

        new DividendMessageBuilder(listOfCompanies, configuration).sendDividendEmail();
        FlexStatement flexStatement = xmlParserForContent.getFlexStatement();


    }

    private static double getRatioGivenCurrency(XmlParser xmlParserForContent, String currency) throws ParseException {
        Optional<ConversionRate> op = xmlParserForContent.getListOfConversionRate()
                .stream()
                .filter(conversionRate -> conversionRate.getFromCurrency().equals(currency))
                .findFirst();
        return op.map(ConversionRate::getRate).orElse(1.00);
    }
}