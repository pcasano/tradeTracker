package com.tradeTracker.configuration;

import com.tradeTracker.Main;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ConfigurationReader {

    public Configuration unmarshal() throws JAXBException {
        InputStream inputStream = Main.class.getResourceAsStream("/config.xml");
        JAXBContext context = JAXBContext.newInstance(Configuration.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Configuration) unmarshaller.unmarshal(inputStream);
    }
}