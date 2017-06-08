package Model.XML;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Model.GlobalParameters;
import Model.Tree.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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
    static final String OPERATOR = "operator";
    static final String TYPE = "type";
    static final String SUBJECT = "subject";
    static final String PREDICATE = "predicate";
    static final String OBJECT = "object";
    static final String NOT = "NOT";

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public void createTreeFromNodeList(NodeList nodeList){
        tasks = new Tasks();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                switch (tempNode.getNodeName())
                {
                    case MOTHER_TASK :
                        NodeList motherTaskNodes = tempNode.getChildNodes();
                        int motherTaskNb = motherTaskNodes.getLength();
                        // creating a mother task for each of them
                        for (int task_m_count = 0; task_m_count < motherTaskNb; task_m_count++) {
                            Node motherTaskNode = motherTaskNodes.item(task_m_count);
                            MotherTask motherTask = createMotherTaskFromNode(motherTaskNode);
                            tasks.addTask(motherTask);
                        }
                        break;
                    case LEAF_TASK :
                        NodeList leafTaskNodes = tempNode.getChildNodes();
                        int leafTaskNb = leafTaskNodes.getLength();
                        for (int task_f_count = 0; task_f_count < leafTaskNb; task_f_count++) {
                            Node leafTaskNode = leafTaskNodes.item(task_f_count);
                            LeafTask leafTask = createLeafTaskFromNode(leafTaskNode);
                            tasks.addTask(leafTask);
                        }
                        break;
                }
            }
        }
    }

    public MotherTask createMotherTaskFromNode(Node motherTaskNode)
    {
        MotherTask motherTask = new MotherTask();
        setAttributes(motherTask, motherTaskNode);
        motherTask = setMotherElements(motherTask, motherTaskNode);
        return motherTask;
    }

    public void setAttributes(Task task, Node taskNode)
    {
        if (taskNode.hasAttributes()) {
            // get attributes and set them
            NamedNodeMap nodeMap = taskNode.getAttributes();
            task.setIdProperty(nodeMap.getNamedItem(ID).getNodeValue());
            task.setIdProperty(nodeMap.getNamedItem(NAME).getNodeValue());
            Node iterativeNode = nodeMap.getNamedItem(GlobalParameters.Nature.ITERATIVE.getBaliseName());
            Node optionalNode = nodeMap.getNamedItem(GlobalParameters.Nature.OPTIONELLE.getBaliseName());
            Node interruptibleNode = nodeMap.getNamedItem(GlobalParameters.Nature.INTERRUPTIBLE.getBaliseName());
            if (iterativeNode != null)
            {
                if (iterativeNode.getNodeValue().equals("true"))
                    task.setNature(GlobalParameters.Nature.ITERATIVE);
                else
                    task.setNature(GlobalParameters.Nature.OPTIONELLE);
            }
            else if (optionalNode != null)
            {
                if (optionalNode.getNodeValue().equals("true"))
                    task.setNature(GlobalParameters.Nature.OPTIONELLE);
                else
                    task.setNature(GlobalParameters.Nature.ITERATIVE);
            }
            else if (interruptibleNode != null)
            {
                if (interruptibleNode.getNodeValue().equals("true"))
                    task.setNature(GlobalParameters.Nature.INTERRUPTIBLE);
                else
                    task.setNature(GlobalParameters.Nature.OPTIONELLE);
            }
        }
    }

    public MotherTask setMotherElements(MotherTask motherTask, Node motherTaskNode)
    {
        if (motherTaskNode.hasChildNodes()) {
            NodeList taskElement = motherTaskNode.getChildNodes();
            for (int task_element_count = 0; task_element_count < taskElement.getLength(); task_element_count++) {
                Node elementNode = taskElement.item(task_element_count);
                switch (elementNode.getNodeName())
                {
                    case CONTEXT :
                        // TODO nothing for the moment
                        break;
                    case SUBTASKS :
                        //motherTask.setSubTaskList(createSubTaskList(elementNode.getChildNodes()));
                        createSubTaskList(motherTask, elementNode.getChildNodes());
                        break;
                    case CONSTRUCTOR :
                        motherTask.setConstructor(elementNode.getAttributes().getNamedItem(TYPE).getNodeValue());
                        // Getting the relations
                        if (elementNode.hasChildNodes())
                            createLinkBetweenDaugther(motherTask,elementNode.getChildNodes());
                            // motherTask.setLinkBetweenDaughters(createLinkBetweenDaugther(motherTask,elementNode.getChildNodes()));
                        break;
                    case CONDITIONS :
                        //motherTask.setConditionList(createConditionFromNode(motherTask,elementNode));
                        createConditionFromNode(motherTask,elementNode.getChildNodes());
                        break;
                }
            }
        }
        return motherTask;
    }

    public LeafTask createLeafTaskFromNode(Node leafTaskNode)
    {
        LeafTask leafTask = new LeafTask();
        setAttributes(leafTask, leafTaskNode);
        leafTask = setLeafElements(leafTask, leafTaskNode);
        return leafTask;
    }

    public LeafTask setLeafElements(LeafTask leafTask, Node leafTaskNode)
    {
        if (leafTaskNode.hasChildNodes()) {
            NodeList taskElement = leafTaskNode.getChildNodes();
            for (int task_element_count = 0; task_element_count < taskElement.getLength(); task_element_count++) {
                Node elementNode = taskElement.item(task_element_count);
                switch (elementNode.getNodeName())
                {
                    case CONTEXT :
                        // TODO nothing for the moment
                        break;
                    case CONDITIONS :
                        //leafTask.setConditionList(createConditionFromNode(leafTask, elementNode));
                        createConditionFromNode(leafTask, elementNode.getChildNodes());
                        break;
                }
            }
        }
        return leafTask;
    }

    public ObservableList<String> createSubTaskList(MotherTask motherTask, NodeList subtasks)
    {
        int subTasksNb = subtasks.getLength();
        for (int subtask_count = 0; subtask_count < subTasksNb; subtask_count++) {
            Node subtaskNode = subtasks.item(subtask_count);
            motherTask.addSubTask(subtaskNode.getAttributes().getNamedItem(ID).getNodeValue());
        }
        return motherTask.getSubTaskList();
    }

    public ObservableList<LinkBetweenDaughter> createLinkBetweenDaugther(MotherTask motherTask, NodeList relationsNodes)
    {
        if (relationsNodes == null)
            return motherTask.getLinkBetweenDaughters();
        int relationsNb = relationsNodes.getLength();
        for (int relation_count = 0; relation_count < relationsNb; relation_count++) {
            Node relationNode = relationsNodes.item(relation_count);
            NamedNodeMap nodeMap = relationNode.getAttributes();
            LinkBetweenDaughter newLink = new LinkBetweenDaughter();
            newLink.setLeftDaughter(nodeMap.getNamedItem("lh").getNodeValue());
            newLink.setRelation(GlobalParameters.RelationAllen.fromString(nodeMap.getNamedItem(OPERATOR).getNodeValue()));
            newLink.setRightDaughter(nodeMap.getNamedItem("rh").getNodeValue());
            motherTask.addLinkBetweenDaugther(newLink);
        }
        return motherTask.getLinkBetweenDaughters();
    }

    public ObservableList<Condition> createConditionFromNode(Task task, NodeList conditionsNode)
    {
        if (conditionsNode == null)
            return task.getConditionList();
        int conditionsNb = conditionsNode.getLength();
        for (int condition_count = 0; condition_count < conditionsNb; condition_count++) {
            Node nodeCondition = conditionsNode.item(condition_count);
            Condition newCondition = new Condition();
            newCondition.setId(nodeCondition.getAttributes().getNamedItem(ID).getNodeValue());
            newCondition.setOperator(nodeCondition.getFirstChild().getNodeName());
            newCondition.setType(nodeCondition.getNodeName());
            if(nodeCondition.hasChildNodes()){
                //newCondition.setAssertionList(createAssertionFromNode(newCondition, nodeCondition.getFirstChild().getChildNodes()));
                createAssertionFromNode(newCondition, nodeCondition.getFirstChild().getChildNodes());
            }
            task.addCondition(newCondition);
        }
        return task.getConditionList();
    }

    public ObservableList<Assertion> createAssertionFromNode(Condition condition, NodeList assertionNodes)
    {
        if (assertionNodes == null)
            return condition.getAssertionList();
        int assertionsNb = assertionNodes.getLength();
        for (int assertion_count=0; assertion_count<assertionsNb; assertion_count++)
        {
            Boolean not = false;
            Node nodeAssertion = assertionNodes.item(assertion_count);
            if (nodeAssertion.getNodeName() == NOT) {
                nodeAssertion = nodeAssertion.getFirstChild();
                not = true;
            }
            Assertion newAssertion = new Assertion();
            newAssertion.setType(nodeAssertion.getAttributes().getNamedItem(TYPE).getNodeValue());
            newAssertion.setNot(not);
            newAssertion.setSubject(nodeAssertion.getFirstChild().getAttributes().getNamedItem(SUBJECT).getNodeValue());
            newAssertion.setPredicate(nodeAssertion.getFirstChild().getAttributes().getNamedItem(PREDICATE).getNodeValue());
            newAssertion.setObject(nodeAssertion.getFirstChild().getAttributes().getNamedItem(OBJECT).getNodeValue());
            condition.addAssertion(newAssertion);
        }
        return condition.getAssertionList();
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
}
