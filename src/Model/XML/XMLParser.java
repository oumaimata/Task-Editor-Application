package Model.XML;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import Model.GlobalParameters;
import Model.Tree.*;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


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
/**
 * Created by ladyn-totorosaure on 22/05/17.
 * This class is used to parse the XML file. To transform the XML file into Objects and the Objects into an XML file.
 */
public class XMLParser {

    // reference on the tasks list : Tasks
    private Tasks tasks;

    static final String TASK = "task";
    static final String MOTHER_TASK = "task_m";
    static final String LEAF_TASK = "tasf_f";
    static final String ID = "id";
    static final String NAME = "name";
    static final String OPTIONAL = "optional";
    static final String CONTEXT = "context";
    static final String SUBTASKS = "subtasks";
    static final String CONSTRUCTOR = "constructor";
    static final String CONDITIONS = "conditions";

    public XMLParser(Tasks tasks) {
        this.tasks = tasks;
    }

    public void createTasksFromXML(String XMLFilePath){
        File file = new File(XMLFilePath);

        try{
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            if (doc.hasChildNodes()) {
                NodeList nodeList = doc.getChildNodes();
                printNodeList(nodeList);

            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void createObjectFromNodeList(NodeList nodeList){
        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                // if the node name is task-m then it's the list of the mother tasks
                if (tempNode.getNodeName().equals(MOTHER_TASK)) {
                    // we get the list of the mother tasks
                    NodeList motherTaskNodes = tempNode.getChildNodes();
                    // creating a mother task for each of them
                    for (int task_m_count = 0; task_m_count < nodeList.getLength(); task_m_count++) {
                        Node motherTaskNode = nodeList.item(task_m_count);
                        MotherTask motherTask = new MotherTask();
                        if (tempNode.hasAttributes()) {
                            // get attributes and set them
                            NamedNodeMap nodeMap = tempNode.getAttributes();
                            for (int i = 0; i < nodeMap.getLength(); i++) {

                                Node node = nodeMap.item(i);
                                if (node.getNodeName().equals(ID)) {
                                    motherTask.setIdProperty(node.getNodeValue());
                                }
                                if (node.getNodeName().equals(NAME)) {
                                    motherTask.setNameProperty(node.getNodeValue());
                                }
                                if (node.getNodeName().equals(GlobalParameters.Nature.ITERATIVE)) {
                                    if (node.getNodeValue().equals("true")) {
                                        motherTask.setNature(GlobalParameters.Nature.ITERATIVE);
                                    }
                                }
                                if (node.getNodeName().equals(OPTIONAL)) {
                                    // TODO nothing for the moment
                                }
                            }
                        }
                        if (motherTaskNode.hasChildNodes()) {
                            NodeList taskElement = motherTaskNode.getChildNodes();
                            for (int task_element_count = 0; task_element_count < nodeList.getLength(); task_element_count++) {
                                Node elementNode = taskElement.item(task_element_count);
                                if (elementNode.getNodeName().equals(CONTEXT)) {
                                    // TODO nothing for the moment
                                }
                                // setting the list of subtasks
                                if (elementNode.getNodeName().equals(SUBTASKS)) {
                                    NodeList subtasks = elementNode.getChildNodes();
                                    for (int subtask_count = 0; subtask_count < nodeList.getLength(); subtask_count++) {
                                        Node subtaskNode = subtasks.item(subtask_count);
                                        if (subtaskNode.hasAttributes()) {
                                            // get attributes and set them
                                            NamedNodeMap nodeMap = subtaskNode.getAttributes();
                                            for (int i = 0; i < nodeMap.getLength(); i++) {
                                                Node node = nodeMap.item(i);
                                                motherTask.getSubTaskList().add(node.getNodeValue());                                            }
                                        }
                                    }
                                }
                                if (elementNode.getNodeName().equals(CONSTRUCTOR)){
                                    // setting the constructor type
                                    GlobalParameters.TypeConstructeur typeConstructeur = null;
                                    String nodeValue = elementNode.getAttributes().item(0).getNodeValue();
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.IND.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.IND;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.SEQ.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.SEQ;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.SEQ_ORD.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.SEQ_ORD;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.PAR.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.PAR;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.PAR_SIM.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.PAR_SIM;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.PAR_START.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.PAR_START;}
                                    if (nodeValue.equals(GlobalParameters.TypeConstructeur.PAR_END.getName())){ typeConstructeur = GlobalParameters.TypeConstructeur.PAR_END;}
                                    motherTask.setConstructor(typeConstructeur);

                                    // getting the relations
                                    NodeList relationsNodes = elementNode.getChildNodes();
                                    for (int relation_count = 0; relation_count < nodeList.getLength(); relation_count++) {
                                        Node relationNode = relationsNodes.item(relation_count);
                                        NamedNodeMap nodeMap = relationNode.getAttributes();
                                        LinkBetweenDaughter newLink = new LinkBetweenDaughter();
                                        newLink.setLeftDaughter(nodeMap.getNamedItem("lh").getNodeValue());
                                        newLink.setRelation(GlobalParameters.RelationAllen.fromString(nodeMap.getNamedItem("operator").getNodeValue()));
                                        newLink.setLeftDaughter(nodeMap.getNamedItem("lh").getNodeValue());
                                        motherTask.getLinkBetweenDaughters().add(newLink);
                                    }
                                }
                                if (elementNode.getNodeName().equals(CONDITIONS)){
                                    // getting the conditions
                                    NodeList conditionsNode = elementNode.getChildNodes();
                                    for (int condition_count = 0; condition_count < conditionsNode.getLength(); condition_count++) {
                                        Node nodeCondition = conditionsNode.item(condition_count);
                                        if (nodeCondition.getNodeName().equals(GlobalParameters.TypeCondition.satisfaction.getName())){
                                            // setting the conditions
                                            Condition newCondition = new Condition();
                                            newCondition.setId(nodeCondition.getAttributes().getNamedItem(ID).getNodeValue());
                                            if(nodeCondition.hasChildNodes()){
                                                NodeList satisfactionNodes = nodeCondition.getChildNodes();
                                                newCondition.setType(GlobalParameters.TypeCondition.satisfaction);
                                                for (int satisfaction_count = 0; satisfaction_count < nodeList.getLength(); satisfaction_count++){
                                                    Node operatorNode = satisfactionNodes.item(satisfaction_count);
                                                    // getting the assertion
                                                    if (operatorNode.getNodeName().equals(GlobalParameters.OperateurLogique.AND)){
                                                        Assertion newAssertion = new Assertion();
                                                        newAssertion.setOperator(GlobalParameters.OperateurLogique.AND);
                                                        if (operatorNode.hasChildNodes()){
                                                            NodeList assertionNodeList = operatorNode.getChildNodes();
                                                            // TODO Continuer le parsing
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void printNodeList(NodeList  nodeList){
        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());

                    }

                }

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNodeList(tempNode.getChildNodes());

                }

                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }
    }


//    @SuppressWarnings({ "unchecked", "null" })
//    public List<Task> readXML(String XMLtext) {
//        ObservableList<Task> tasksListTemp = tasks.getTasks();
//        try {
//            // First, create a new XMLInputFactory
//            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//            // Setup a new eventReader
//            InputStream in = new ByteArrayInputStream(XMLtext.getBytes(StandardCharsets.UTF_8));
//            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
//            // read the XML document
//            Task task = null;
//
//            while (eventReader.hasNext()) {
//                XMLEvent event = eventReader.nextEvent();
//
//                if (event.isStartElement()) {
//                    StartElement startElement = event.asStartElement();
//                    // If we have an item element, we create a new item
//                    if (startElement.getName().getLocalPart().equals(TASK)) {
//                        task = new Task();
//                        // We read the attributes from this tag and add the date
//                        // attribute to our object
//                        Iterator<Attribute> attributes = startElement.getAttributes();
//                        while (attributes.hasNext()) {
//                            Attribute attribute = attributes.next();
//                            if (attribute.getName().toString().equals(ID)) {
//                                task.setIdProperty(attribute.getValue());
//                            }
//                            if (attribute.getName().toString().equals(GlobalParameters.Nature.ITERATIVE.getBaliseName())) {
//                                if(attribute.getValue() == "true"){
//                                    task.setNature(GlobalParameters.Nature.ITERATIVE);
//                                }
//                            }
//                            if (attribute.getName().toString().equals(NAME)) {
//                                task.setNameProperty(attribute.getValue());
//                            }
//                        }
//                        event = eventReader.nextEvent();
//                    }
//
//                    if (event.isStartElement()) {
//                        if (event.asStartElement().getName().getLocalPart().equals(REF_CONCEPT)) {
//
//                            continue;
//                        }
//                    }
//                    if (event.asStartElement().getName().getLocalPart()
//                            .equals(UNIT)) {
//                        event = eventReader.nextEvent();
//                        item.setUnit(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart()
//                            .equals(CURRENT)) {
//                        event = eventReader.nextEvent();
//                        item.setCurrent(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart()
//                            .equals(INTERACTIVE)) {
//                        event = eventReader.nextEvent();
//                        item.setInteractive(event.asCharacters().getData());
//                        continue;
//                    }
//                }
//                // If we reach the end of an item element, we add it to the list
//                if (event.isEndElement()) {
//                    EndElement endElement = event.asEndElement();
//                    if (endElement.getName().getLocalPart().equals(ITEM)) {
//                        items.add(item);
//                    }
//                }
//
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }
//        return items;
//    }

}
