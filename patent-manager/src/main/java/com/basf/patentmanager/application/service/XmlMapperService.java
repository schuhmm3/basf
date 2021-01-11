package com.basf.patentmanager.application.service;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.domain.model.Patent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Service to map an XML into a Patent
 *
 * @author robertogomez
 */
@Service
@Slf4j
public class XmlMapperService {

    /**
     * Maps a file into a patent
     *
     * @param stream      Inputstream from the file to map into a patent
     * @param fieldsPaths Paths to retrieve from XML
     * @return The patent
     */
    Patent createPatentFromXml(InputStream stream, PatentFieldsPaths fieldsPaths) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(fieldsPaths, Map.class);
        AtomicReference<Document> xmlDocument = new AtomicReference<>();
        AtomicReference<XPath> xPath = new AtomicReference<>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            xmlDocument.set(builder.parse(stream));
            xPath.set(XPathFactory.newInstance().newXPath());
            map = map.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> getXmlValue(xmlDocument.get(), xPath.get(), entry)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new PatentException(ApplicationError.PARSING_FILE_ERROR, e);
        }
        return mapper.convertValue(map, Patent.class);
    }


    /**
     * Gets the value from the xml path
     *
     * @param xmlDocument XMLDocument that contains all the information from the patent
     * @param xPath       Interface to access a path to the XMLDocument
     * @param entry       Key-value pair with the field name and the xml path to retrieve it
     * @return The text content retrieved from the XML path
     */
    Serializable getXmlValue(Document xmlDocument, XPath xPath, Map.Entry<String, Object> entry) {
        try {
            String value = "";
            String fieldPath = (String) entry.getValue();
            Node xmlNode = ((NodeList) xPath.compile(fieldPath).evaluate(xmlDocument, XPathConstants.NODESET)).item(0);
            if (xmlNode != null) {
                value = xmlNode.getTextContent().strip().replaceAll("(\\n( )+)|(( )+\\n)", "\n");
                if (entry.getKey().equals("date"))
                    return new SimpleDateFormat("yyyymmdd").parse(value);
                return value;
            } else {
                log.warn("Couldn't map {}", entry.getKey());
            }
            return value;
        } catch (XPathExpressionException | ParseException e) {
            throw new PatentException(ApplicationError.PARSING_FILE_ERROR, e);
        }
    }

}
