package com.tradeTracker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.configuration.ConfigurationReader;
import javax.xml.bind.JAXBException;

public class Main {

    public static void main(String[] args) throws JAXBException {

        Configuration configuration = new ConfigurationReader().unmarshal();

        RestAssured.baseURI = configuration.getBaseUrl();
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("t", configuration.getToken())
                .queryParam("q", configuration.getQueryId())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.SendRequest");
        String jsonString = response.asString();

        RequestSpecification request_ = RestAssured.given();
        Response response_ = request_.queryParam("q", "4937469989")
                .queryParam("t", configuration.getToken())
                .queryParam("v", configuration.getApiVersion())
                .get("FlexStatementService.GetStatement");
        response_.asString();
    }

}