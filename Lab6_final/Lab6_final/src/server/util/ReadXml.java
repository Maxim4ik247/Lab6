package server.util;


import data.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadXml {
    public static List<Worker> read(String path) {
        try {

            ArrayList<Worker> list = new ArrayList<>();

            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList objectList = doc.getElementsByTagName("Worker");

            for (int i = 0; i < objectList.getLength(); i++) {
                Element objectElement = (Element) objectList.item(i);

//                Worker worker = new Worker();
                Coordinates coordinates = new Coordinates(
                        Float.parseFloat(objectElement.getElementsByTagName("x").item(0).getTextContent()),
                        Float.parseFloat(objectElement.getElementsByTagName("y").item(0).getTextContent())
                );
                Location location = new Location(
                        Float.parseFloat(objectElement.getElementsByTagName("x").item(1).getTextContent()),
                        Float.parseFloat(objectElement.getElementsByTagName("y").item(1).getTextContent()),
                        Long.parseLong(objectElement.getElementsByTagName("z").item(0).getTextContent()),
                        objectElement.getElementsByTagName("x").item(1).getTextContent()
                        );

                Person person = new Person(
                        Integer.parseInt(objectElement.getElementsByTagName("height").item(0).getTextContent()),
                        Color.valueOf(objectElement.getElementsByTagName("eyeColor").item(0).getTextContent()),
                        Color.valueOf(objectElement.getElementsByTagName("hairColor").item(0).getTextContent()),
                        Country.valueOf(objectElement.getElementsByTagName("nationality").item(0).getTextContent()),
                        location
                        );

                Worker worker = new Worker(Integer.parseInt(objectElement.getElementsByTagName("id").item(0).getTextContent()),
                        objectElement.getElementsByTagName("name").item(0).getTextContent(),
                        coordinates,
                        LocalDateTime.parse(objectElement.getElementsByTagName("creationDate").item(0).getTextContent()),
                        Float.parseFloat(objectElement.getElementsByTagName("salary").item(0).getTextContent()),
                        Position.valueOf(objectElement.getElementsByTagName("position").item(0).getTextContent()),
                        Status.valueOf(objectElement.getElementsByTagName("status").item(0).getTextContent()),
                        person
                );


                list.add(worker);

            }

            return list;

        } catch (ParserConfigurationException | SAXException | IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
