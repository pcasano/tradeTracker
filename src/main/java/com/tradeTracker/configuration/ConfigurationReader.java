package com.tradeTracker.configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;

public class ConfigurationReader {

    public Configuration unmarshal() throws JAXBException {
        File file = new File("src/main/resources/config.xml");
        JAXBContext context = JAXBContext.newInstance(Configuration.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Configuration) unmarshaller.unmarshal(file);
    }
}