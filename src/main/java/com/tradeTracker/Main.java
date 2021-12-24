package com.tradeTracker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.configuration.ConfigurationReader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, SAXException {

        Configuration configuration = new ConfigurationReader().unmarshal();

        RestAssured.baseURI = configuration.getBaseUrl();
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("t", configuration.getToken())
                .queryParam("q", configuration.getQueryId())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.SendRequest_");
        String xmlString = response.asString();

        String referenceCode = new XmlParser(xmlString).getReferenceCode();


        RequestSpecification request_ = RestAssured.given();
        Response response_ = request_.queryParam("q", "4937469989")
                .queryParam("t", configuration.getToken())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.GetStatement");
        response_.asString();
    }

}