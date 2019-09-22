package com.beaconyx.api;

import com.beaconyx.ConstantDepot;
import com.beaconyx.log.MyLogger;
import com.beaconyx.model.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public List<Item> parse(String xmlString) {
        InputSource fileSource = new InputSource(new StringReader(xmlString));
        return parsing(fileSource);
    }

    public List<Item> parse(File xmlFile) {
        if (!xmlFile.isFile())
            return null;

        try {
            InputSource fileSource = new InputSource(new FileReader(xmlFile));
            return parsing(fileSource);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private List<Item> parsing(InputSource is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expr = xPath.compile("//items/item");
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            List<Item> items = new ArrayList<>(nodeList.getLength());
            for (int i= 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                Item item = new Item();
                item.setContentid(node.getElementsByTagName(ConstantDepot.COL_CONTENT_ID).item(0).getTextContent());
                item.setEventstartdate(node.getElementsByTagName(ConstantDepot.COL_EVENT_START_DATE).item(0).getTextContent());
                item.setEventenddate(node.getElementsByTagName(ConstantDepot.COL_EVENT_END_DATE).item(0).getTextContent());
                if (node.getElementsByTagName(ConstantDepot.COL_FIRST_IMAGE).item(0) == null)
                    item.setFirstimage(ConstantDepot.NO_IMAGE_URL);
                else
                    item.setFirstimage(node.getElementsByTagName(ConstantDepot.COL_FIRST_IMAGE).item(0).getTextContent());

                if (node.getElementsByTagName(ConstantDepot.COL_MAP_X).item(0) == null) {
                    item.setMapx("0.0");
                    item.setMapy("0.0");
                } else {
                    item.setMapx(node.getElementsByTagName(ConstantDepot.COL_MAP_X).item(0).getTextContent());
                    item.setMapy(node.getElementsByTagName(ConstantDepot.COL_MAP_Y).item(0).getTextContent());
                }

                item.setModifiedtime(node.getElementsByTagName(ConstantDepot.COL_MODIFIED_TIME).item(0).getTextContent());
                item.setTitle(node.getElementsByTagName(ConstantDepot.COL_TITLE).item(0).getTextContent());

                if (node.getElementsByTagName(ConstantDepot.COL_READ_COUNT).item(0) == null)
                    item.setReadcount("0");
                else
                    item.setReadcount(node.getElementsByTagName(ConstantDepot.COL_READ_COUNT).item(0).getTextContent());

                items.add(item);
            }

            return items;
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
            MyLogger.z(e.getMessage());
        }

        return null;
    }
}
