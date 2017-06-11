package Model.XML;

import java.io.File;

import Model.GlobalParameters;
import Model.Tree.*;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


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

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    // reference on the tasks list : Tasks
    private Tasks tasks;

    static final String TASKS = "tasks";
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

    public Boolean isElement(Node node)
    {
        return (node.getNodeType() ==  Node.ELEMENT_NODE);
    }

    public XMLParser(Tasks tasks) {
        this.tasks = tasks;
    }

    public void createTasksFromXML(String XMLFilePath){
        tasks = new Tasks();
        File file = new File(XMLFilePath);

        try{
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            if (doc.hasChildNodes()) {
                NodeList nodeList = doc.getDocumentElement().getChildNodes();
                for (int count = 0; count < nodeList.getLength(); count++) {
                    Node node = nodeList.item(count);
                    System.out.println("tested node :" + node.getNodeName());
                    if (node.getNodeName().equals(TASKS)) {
                        NodeList tasksTypeList = node.getChildNodes();
                        createTreeFromNodeList(tasksTypeList);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public void createTreeFromNodeList(NodeList nodeList){
        for (int count = 0; count < nodeList.getLength(); count++) {
            System.out.println("nodeList count : "+count);
            System.out.println("nodeList length : "+nodeList.getLength());
            Node tempNode = nodeList.item(count);
            System.out.println(tempNode.toString());
            // make sure it's element node.
            if (isElement(tempNode)) {
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
                            System.out.println("test mother: "+isElement(motherTaskNode));
                            if (isElement(motherTaskNode))
                            {
                                MotherTask motherTask = createMotherTaskFromNode(motherTaskNode);
                                tasks.addTask(motherTask);
                                System.out.println("mothertask "+motherTask.toString());
                            }
                        }
                        break;
                    case LEAF_TASK :
                        NodeList leafTaskNodes = tempNode.getChildNodes();
                        int leafTaskNb = leafTaskNodes.getLength();
                        for (int task_f_count = 0; task_f_count < leafTaskNb; task_f_count++) {
                            Node leafTaskNode = leafTaskNodes.item(task_f_count);
                            System.out.println("test leaf: "+isElement(leafTaskNode));
                            if (isElement(leafTaskNode))
                            {
                                LeafTask leafTask = createLeafTaskFromNode(leafTaskNode);
                                tasks.addTask(leafTask);
                                System.out.println("leaftask "+leafTask.toString());
                            }
                        }
                        break;
                }
            }
        }
    }

    public MotherTask createMotherTaskFromNode(Node motherTaskNode)
    {
        MotherTask motherTask = new MotherTask();
        System.out.println("init mother task ");
        System.out.println(motherTask.toString());
        motherTask = (MotherTask)setAttributes(motherTask, motherTaskNode);
        System.out.println("attributes set"+motherTask.toString());
        motherTask = setMotherElements(motherTask, motherTaskNode);
        System.out.println("elements set"+motherTask.toString());
        return motherTask;
    }

    public Task setAttributes(Task task, Node taskNode)
    {
        if (taskNode.hasAttributes()) {
            // get attributes and set them
            NamedNodeMap nodeMap = taskNode.getAttributes();
            task.setIdProperty(nodeMap.getNamedItem(ID).getNodeValue());
            System.out.println("Id : "+task.getIdProperty());
            task.setIdProperty(nodeMap.getNamedItem(NAME).getNodeValue());
            Node iterativeNode = nodeMap.getNamedItem(GlobalParameters.Nature.ITERATIVE.getName());
            Node optionalNode = nodeMap.getNamedItem(GlobalParameters.Nature.OPTIONELLE.getName());
            Node interruptibleNode = nodeMap.getNamedItem(GlobalParameters.Nature.INTERRUPTIBLE.getName());
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
            else
            {
                if (interruptibleNode != null && interruptibleNode.getNodeValue().equals("true"))
                    task.setNature(GlobalParameters.Nature.INTERRUPTIBLE);
                else
                    task.setNature(GlobalParameters.Nature.OPTIONELLE);
            }
            System.out.println("Nature : "+task.getNature().getName());
        }
        return task;
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
                        System.out.println(motherTask.getConstructor().getName());
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
        System.out.println("Filles :");
        for (int subtask_count = 0; subtask_count < subTasksNb; subtask_count++) {
            Node subtaskNode = subtasks.item(subtask_count);
            if (isElement(subtaskNode))
            {
                motherTask.addSubTask(subtaskNode.getAttributes().getNamedItem(ID).getNodeValue());
                System.out.print("  ");
                System.out.println(subtaskNode.getAttributes().getNamedItem(ID).getNodeValue()+", ");
            }
        }
        System.out.println(" ");
        return motherTask.getSubTaskList();
    }

    public ObservableList<LinkBetweenDaughter> createLinkBetweenDaugther(MotherTask motherTask, NodeList relationsNodes)
    {
        if (relationsNodes == null)
            return motherTask.getLinkBetweenDaughters();
        int relationsNb = relationsNodes.getLength();
        System.out.println("Relations :");
        for (int relation_count = 0; relation_count < relationsNb; relation_count++) {
            Node relationNode = relationsNodes.item(relation_count);
            if (isElement(relationNode))
            {
                System.out.print("  ");
                NamedNodeMap nodeMap = relationNode.getAttributes();
                LinkBetweenDaughter newLink = new LinkBetweenDaughter();
                newLink.setLeftDaughter(nodeMap.getNamedItem("lh").getNodeValue());
                newLink.setRelation(GlobalParameters.RelationAllen.fromString(nodeMap.getNamedItem(OPERATOR).getNodeValue()));
                newLink.setRightDaughter(nodeMap.getNamedItem("rh").getNodeValue());
                System.out.println(newLink.getLeftDaughter()+" "+newLink.getRelation().getName()+" "+newLink.getRightDaughter());
                motherTask.addLinkBetweenDaugther(newLink);
            }
        }
        return motherTask.getLinkBetweenDaughters();
    }

    public ObservableList<Condition> createConditionFromNode(Task task, NodeList conditionsNode)
    {
        if (conditionsNode == null)
            return task.getConditionList();
        int conditionsNb = conditionsNode.getLength();
        System.out.println("Conditions :");
        for (int condition_count = 0; condition_count < conditionsNb; condition_count++) {
            Node nodeCondition = conditionsNode.item(condition_count);
            if (isElement(nodeCondition))
            {
                Condition newCondition = new Condition();
                newCondition.setId(nodeCondition.getAttributes().getNamedItem(ID).getNodeValue());
                newCondition.setType(nodeCondition.getNodeName());
                System.out.println("  Type : "+newCondition.getType().getName());
                int nb = nodeCondition.getChildNodes().getLength();
                NodeList childs = nodeCondition.getChildNodes();
                System.out.println("  Assertions : ");
                for (int i=0; i<nb; ++i)
                {
                    Node operatorNode = childs.item(i);
                    if (isElement(operatorNode))
                    {
                        newCondition.setOperator(operatorNode.getNodeName());
                        createAssertionFromNode(newCondition, operatorNode.getChildNodes());
                    }
                }
                task.addCondition(newCondition);
            }
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
            if (isElement(nodeAssertion))
            {
                if (nodeAssertion.getNodeName() == NOT) {
                    NodeList list = nodeAssertion.getChildNodes();
                    int nbNodes = list.getLength();
                    nodeAssertion = nodeAssertion.getFirstChild();
                    int i = 0;
                    while (i<nbNodes || isElement(nodeAssertion))
                    {
                        nodeAssertion = list.item(i++);
                    }
                    not = true;
                }
                Assertion newAssertion = new Assertion();
                newAssertion.setType(nodeAssertion.getAttributes().getNamedItem(TYPE).getNodeValue());
                newAssertion.setNot(not);
                NodeList triplets = nodeAssertion.getChildNodes();
                int nbTriplets = triplets.getLength();
                for (int i=0; i< nbTriplets; ++i)
                {
                    Node triplet = triplets.item(i);
                    if (isElement(triplet))
                    {
                        newAssertion.setSubject(triplet.getAttributes().getNamedItem(SUBJECT).getNodeValue());
                        newAssertion.setPredicate(triplet.getAttributes().getNamedItem(PREDICATE).getNodeValue());
                        newAssertion.setObject(triplet.getAttributes().getNamedItem(OBJECT).getNodeValue());
                    }

                }
                System.out.println("        "+newAssertion.getSubject()+" "+newAssertion.getPredicate()+" "+newAssertion.getSubject());
                condition.addAssertion(newAssertion);
            }
        }
        return condition.getAssertionList();
    }

    public void printNodeList(NodeList  nodeList){
        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (isElement(tempNode)) {

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
