package Model.XML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by ladyn-totorosaure on 22/05/17.
 */
public class XMLParser {
    /* soit paring DOM soit parsing SAX
    pour parsing SAX
    Reader et listener, quand événement fonction de réponse
    reader est déjà prêt
    listener à faire,
    événement détecté par le reader ex : balise ouvrante, balise fermante
    récupére le contexte grâce à une fonction

    dom crée un arbre des noeuds pas rès pratique dans
    notre cas car ne correspond pas du tout à notre type d'arborescence

    se renseigner sur xpath
    librairie Xerces

    faire des scénarios en fonction des uses cases choisir le pasingc
    utiliser la classe XMLReader ?

    * */

    public static void main (String[] args) throws ParserConfigurationException{
        XMLParser t = new XMLParser();
        t.readXml() ;
    }
    public void readXml () throws ParserConfigurationException{
        File fXmlFile = new File("exempleXML.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = null;
        try {
            doc = dBuilder.parse(fXmlFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("task");

        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

//                System.out.println("Item No : " + eElement.getElementsByTagName("item_no").item(0).getTextContent());
//                System.out.println("Description : " + eElement.getElementsByTagName("description").item(0).getTextContent());
//                System.out.println("price : " + eElement.getElementsByTagName("price").item(0).getTextContent());
//                System.out.println("base qty : " + eElement.getElementsByTagName("base_qty").item(0).getTextContent());
//                System.out.println("Var qty : " + eElement.getElementsByTagName("var_qty").item(0).getTextContent());
//                System.out.println("Base price : " + eElement.getElementsByTagName("base_price_").item(0).getTextContent());

            }}}}