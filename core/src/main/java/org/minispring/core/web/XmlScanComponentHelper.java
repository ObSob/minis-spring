package org.minispring.core.web;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlScanComponentHelper {

    public static List<String> getNodeValue(URL resource) {
        SAXBuilder builder = new SAXBuilder();
        List<String> basePackages = new ArrayList<>();

        try {
            InputStream inputStream = resource.openStream();
            Document document = builder.build(inputStream);
            Element rootNode = document.getRootElement();
            List<Element> componentScans = rootNode.getChildren("component-scan");

            for (Element componentScan : componentScans) {
                basePackages.add(componentScan.getAttributeValue("base-package"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basePackages;
    }
}
