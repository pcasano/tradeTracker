package com.tradeTracker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.io.IOException;

public class XmlParser {

    private String xmlString;

    public XmlParser(String xmlString) {
        this.xmlString = xmlString;
    }

    private Document buildDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        return builder.parse(is);
    }

    public String getReferenceCode() throws ParserConfigurationException, IOException, SAXException {
        Document doc = this.buildDocument();
        return doc.getElementsByTagName("ReferenceCode").item(0).getTextContent();
    }
}
