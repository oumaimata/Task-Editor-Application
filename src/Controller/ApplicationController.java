package Controller;

import Model.GlobalParameters;
import Model.Tree.*;
import Model.XML.XMLFile;
import Model.XML.XMLParser;
import View.XMLEditor;
import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.hierarchic.HierarchicLayout;
import com.yworks.yfiles.layout.organic.OrganicLayout;
import com.yworks.yfiles.layout.orthogonal.OrthogonalLayout;
import com.yworks.yfiles.layout.tree.TreeLayout;
import com.yworks.yfiles.utils.IEventHandler;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.ClickEventArgs;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import com.yworks.yfiles.view.input.ICommand;
import com.yworks.yfiles.view.input.ItemClickedEventArgs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class ApplicationController {

    // reference sur le controller du graph
    public GraphControl graphControl;
    // reference sur le controller de la vue
    //public ViewController view;
    // reference sur l'ensemble des taches du modèle
    public Tasks tasks;
    // reference sur l'ensemble des taches meres du modèle
    private MotherTasks motherTasks;
    // reference sur l'ensemble des taches feuilles du modèle
    private LeafTasks leafTasks;
    // reference sur le noeud selectionné (dans le cas du graph notamment)
    private INode currentNode;
    // reference sur la tache lorsque c'est une tache mere
    private MotherTask currentMotherTask;
    // reference sur la tache lorsque c'est une tache feuille
    private LeafTask currentLeafTask;
    // reference sur la tache lorsque c'est une tache
    private Task currentTask;
    // reference sur la liste des noeuds
    public Nodes nodes;
    //reference sur la liste des tags
    public Tags tags;
    // generated style
    private ShapeNodeStyle MotherTaskStyle;
    private ShapeNodeStyle TaskStyle;
    private ShapeNodeStyle LeafTaskStyle;
    // edition du graph
    private GraphEditorInputMode graphEditorInputMode;
    private LeafTask createdLeafTask;
    private MotherTask createdMotherTask;
    //
    // savoir si le panel est relevé
    //Boolean panelActif;
    // contient la chaine de caractére du XML
    private XMLFile xmlFile;
    // le parser pour le XML
    private XMLParser xmlParser;
    // layout utilisé
    private TreeLayout treeLayout;
    private OrganicLayout organicLayout;
    private HierarchicLayout hierarchicLayout;
    private OrthogonalLayout orthogonalLayout;



    // le container de l'editeur XML

    private VirtualizedScrollPane vScrollPane;
    // l'editeur
    private CodeArea codeArea;
    // border pane contenant l'éditeur
    @FXML
    BorderPane borderPane;

    // bar de menu
    @FXML
    private  Button button_save,button_undo,button_redo,button_centrer,button_hierarchiser, button_open;
    // Graph
    @FXML
    private  Button button_graph_ajouter,button_graph_supprimer, button_graph_ajouter_tag, button_graph_supprimer_tag;
    @FXML
    private VBox vbox_tags;
    @FXML
    private  ListView<Tag> listview_tags;

    // XML
    @FXML
    private  Button button_xml_rafraichir;


    @FXML
    private TabPane panel;
    @FXML
    private Tab tab_resume;
    @FXML
    private Tab tab_liens;
    @FXML
    private Tab tab_condition;

    // Panel d'édition - Resume
    @FXML
    private  Button button_edit_ajout_tache_fille,button_edit_retrait_tache_fille,button_edit_enregistrer_resume;
    @FXML
    private  TextField txtfield_edit_name;
    @FXML
    private ComboBox<String> cbb_nature, cbb_constructeur;
    @FXML
    private  ListView<String> listview_edit_taches_filles,listview_edit_autres_taches;
    @FXML
    private  Text txt_edit_id_resume;

    @FXML
    private Text label_nature, label_constructeur,label_filles,label_autres;

    // Panel d'édition - Liens
    @FXML
    private  Button button_edit_enregistrer_liens;

    @FXML
    private ComboBox<String> cbb_tache_fille_1, cbb_tache_fille_2, cbb_lien_taches;
    @FXML
    private  Text txt_edit_id_liens;

    // Panel d'édition - Conditions
    @FXML
    private  Button button_edit_ajouter_conditions,button_edit_supprimer_conditions,button_edit_ajouter_assertions,button_edit_modifier_assertions,button_edit_supprimer_assertions,button_edit_enregistrer_conditions;
    @FXML
    private  Text txt_edit_nom_tache_conditions,txt_edit_condition_selectionne;
    @FXML
    private  ListView<Condition> listview_edit_conditions;
    @FXML
    private  ListView<Assertion> listview_edit_assertions;
    @FXML
    private ComboBox<String> cbb_type_condition, cbb_operateur_condition;

    // Split pane
    @FXML
    private SplitPane splitPane_graph_edit;

    // test
    @FXML
    private TextField popup_textfield_id, popup_textfield_name;

    public ApplicationController() {
        //currentTask = new Task();
        graphControl = new GraphControl();
        // reference sur l'edition du graph
        graphEditorInputMode = new GraphEditorInputMode();
        // mise en place de preference sur les objets des click : d'abord les noeuds, puis les fleches, puis les labels
        graphEditorInputMode.setClickHitTestOrder(
                new GraphItemTypes[]{
                        GraphItemTypes.NODE.or(GraphItemTypes.EDGE),
                        GraphItemTypes.LABEL});
        // empecher la création d'un noeud au clic sur le graphique
        graphEditorInputMode.setCreateNodeAllowed(false);
        graphControl.setInputMode(graphEditorInputMode);
        // creation des styles
        MotherTaskStyle = createMotherTaskStyle();
        TaskStyle = createTaskStyle();
        LeafTaskStyle = createLeafStyle();
        //currentTask = new Task();
        // le panel est actif initialement
        //panelActif = true;
        // initialisation des layout utilisés
        treeLayout = new TreeLayout();
        organicLayout = new OrganicLayout();
        organicLayout.setConsiderNodeSizes(true);
        organicLayout.setMinimumNodeDistance(50);
        hierarchicLayout = new HierarchicLayout();
        hierarchicLayout.setMinimumLayerDistance(50);
        hierarchicLayout.setNodeLabelConsiderationEnabled(true);
        orthogonalLayout = new OrthogonalLayout();
        // initialisation du xmlFile
        xmlFile = new XMLFile();
        // initialisation des noeuds courrant
        currentMotherTask = null;
        currentLeafTask = null;
        //currentTask = null;
    }

    public void initialize() {
        assert listview_edit_assertions != null;
        // permettre l'edition directe du graph
        // ensemble des tâches à null initialement.
        // Dans le cas d'un chargement il faudra modifier directement cette valeur
        tasks = new Tasks();
        // initialisation de la liste des noeuds meres
        motherTasks = new MotherTasks();
        // initialisation de la liste des noeuds feuilles
        leafTasks = new LeafTasks();
        // initialisation de la liste des noeuds
        nodes = new Nodes();
        // initilisation de l'objet qui contient l'adresse du fichier et le texte de ce fichier
        xmlFile = new XMLFile();
        xmlParser = new XMLParser(tasks);

        // on creer le graph associé
        createGraphFromTasks(motherTasks,tasks,leafTasks);

        //Creation de la liste des tags
        tags = new Tags();

        //PopupController popup = new PopupController();
        // creation de l'application controller
        // permettre l'edition directe du graph
        //graphControl.setInputMode(new GraphEditorInputMode());
        // on fait les bindings depuis applicationController
        make_binding();
        // on déclanche la méthode d'action principale du controller de l'application
        main_action();
        // initialisation de l'éditeur de code
        codeArea = new CodeArea();
        vScrollPane = new VirtualizedScrollPane(codeArea);
        borderPane.setCenter(vScrollPane);

        // montrer les numeros de lignes
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, XMLEditor.computeHighlighting(newText));
            xmlFile.setXMLtext(newText);
        });
        codeArea.replaceText(0, 0, xmlFile.getXMLtext());
        // setting the action to open a document
        button_open.setOnAction((event) -> openFile());
        button_save.setOnAction((event) -> save());

        button_xml_rafraichir.setOnAction((event) -> {refreshTreeFromXML();});
        xmlFile.XMLtextProperty().bindBidirectional(codeArea.accessibleTextProperty());

        cbb_nature.getItems().addAll("interruptible", "optional","iterative");
        cbb_constructeur.getItems().addAll("IND","SEQ","SEQ_ORD","PAR","PAR_SIM","PAR_START","PAR_END");

        cbb_lien_taches.getItems().addAll(">","<","m","mi","o","oi","s","si","d","di","f","fi","=");

        cbb_type_condition.getItems().addAll("nomological","satisfaction","arret");
        cbb_operateur_condition.getItems().addAll("AND","OR","XOR","NOT");

//        bindingTaskAndEdition();
    }

    public void adjust_panel(boolean isMotherTask)
    {
        label_constructeur.setVisible(isMotherTask);
        cbb_constructeur.setVisible(isMotherTask);
        label_nature.setVisible(isMotherTask);
        cbb_nature.setVisible(isMotherTask);
        button_edit_ajout_tache_fille.setVisible(isMotherTask);
        button_edit_retrait_tache_fille.setVisible(isMotherTask);
        listview_edit_taches_filles.setVisible(isMotherTask);
        label_filles.setVisible(isMotherTask);
        listview_edit_autres_taches.setVisible(isMotherTask);
        label_autres.setVisible(isMotherTask);
        tab_liens.setDisable(!isMotherTask);
    }

    // Méthode principale a utiliser dans le controller.
    // Y effectuer toutes les actions
    public void main_action (){
        graphControl.currentItemProperty();

        getListview_tags().setItems(tags.getTags());

    }

    // Méthode pour réaliser les bindings des actions et des boutons

    public void make_binding (){

        // creation du listener sur le bouton pour centrer le graph
        getButton_centrer().setOnAction(evt -> graphFitContent());

        // ajout du listener sur le bouton de hierarchisation
        getButton_hierarchiser().setOnAction(evt -> handleLayoutAction(evt, organicLayout));

        // ajout du listener sur le bouton de hierarchisation
        getButton_graph_supprimer().setOnAction(evt -> {
            handleSuppressionNoeud();
        });

        // creation du listener sur le click d'un objet
        graphEditorInputMode.addItemLeftClickedListener(new IEventHandler<ItemClickedEventArgs<IModelItem>>() {
            @Override
            public void onEvent(Object o, ItemClickedEventArgs<IModelItem> iModelItemItemClickedEventArgs) {
                if(iModelItemItemClickedEventArgs.getItem().getTag().getClass() == Task.class ||
                        iModelItemItemClickedEventArgs.getItem().getTag().getClass() ==  MotherTask.class ||
                        iModelItemItemClickedEventArgs.getItem().getTag().getClass() ==  LeafTask.class ){
                    System.out.println("l'objet sélectionné est un noeud de tache: " + iModelItemItemClickedEventArgs.getItem() );
                    // on récupère l'objet sélectionné
                    currentNode = (INode) iModelItemItemClickedEventArgs.getItem();
                    System.out.println("l'objet courrant est devenu: " + currentNode.getTag().getClass() );
                    if (currentNode.getTag().getClass() == MotherTask.class){
                        System.out.println("l'objet dans current node est une tache mere" );
                        currentMotherTask = (MotherTask) currentNode.getTag();
                        //currentTask=null;
                        currentLeafTask= null;
                    }else{
                        System.out.println("l'objet dans current node est une tache feuille" );
                        currentLeafTask = (LeafTask) currentNode.getTag();
                        //currentTask=null;
                        currentMotherTask=null;
                    }

                    adjust_panel(currentMotherTask != null);
                    changePanelState(true);
                    if(currentMotherTask != null)
                    {
                        fillPanel(currentMotherTask);
                    }
                    else if (currentLeafTask != null)
                    {
                        fillPanel(currentLeafTask);
                    }
                }
                // mise en place du panel d'édition
                changePanelState(true);
            }
        });

        // creation du listener sur le click d'une zone non clickable du graph : typiquement le fond du graph
        graphEditorInputMode.addCanvasClickedListener(new IEventHandler<ClickEventArgs>() {
            @Override
            public void onEvent(Object o, ClickEventArgs clickEventArgs) {
                // modification de l'etat du panel d'édition
                changePanelState(false);
                // on supprime la tache courante sélectionnée
                currentNode = null;
                currentMotherTask = null;
                currentLeafTask = null;
                //currentTask = null;
            }
        });

        listview_edit_conditions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Condition>() {
            @Override
            public void changed(ObservableValue<? extends Condition> observable, Condition oldValue, Condition newValue) {
                if (newValue != null)
                {
                    cbb_type_condition.setValue(newValue.getType().getName());
                    cbb_operateur_condition.setValue(newValue.getOperator().getName());
                    listview_edit_assertions.getItems().clear();
                    listview_edit_assertions.setItems(newValue.getAssertionList());
                    txt_edit_condition_selectionne.setText(newValue.toString());
                }
                else
                {
                    listview_edit_assertions.getItems().clear();
                    cbb_type_condition.setValue("");
                    cbb_operateur_condition.setValue("");
                    txt_edit_condition_selectionne.setText("");
                }
            }
        });


        //Binding entre currentTask et PanelEdition

        graphEditorInputMode
                .getCreateEdgeInputMode()
                .setEdgeCreator((context, graph, sourceCandidate, targetCandidate, dummyEdge) -> {
                    // get the source and target ports from the candidates
                    IPort sourcePort= sourceCandidate.getPort();
                    if (sourcePort == null) {
                        sourcePort = sourceCandidate.createPort(context);
                    }
                    IPort targetPort = targetCandidate.getPort();
                    if (targetPort == null) {
                        targetPort = targetCandidate.createPort(context);
                    }
                    // create the edge between the source and target port
                    IEdge edge = graph.createEdge(sourcePort, targetPort, dummyEdge.getStyle());
                    // creation d'un lien entre pere et fils
                    createLinkBetweenTwoNodes(graph, edge.getSourceNode(),edge.getTargetNode(),edge);

                    return edge;
                });
        graphEditorInputMode.getCreateEdgeInputMode().addEdgeCreatedListener(new IEventHandler<EdgeEventArgs>() {
            @Override
            public void onEvent(Object o, EdgeEventArgs edgeEventArgs) {

            }
        });
        graphControl.setInputMode(graphEditorInputMode);
        // listener sur tags
        getButton_graph_ajouter_tag().setOnAction(evt -> handleAddTag());
        getButton_graph_supprimer_tag().setOnAction(evt -> handleDeleteTag());


        //Edit tags names
        class TagListCell extends ListCell<Tag> {
            private final TextField textField = new TextField();

            private TagListCell() {
                textField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                    if (e.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
                textField.setOnAction(e -> {
                    getItem().setNameProperty(textField.getText());
                    setText(textField.getText());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                });
                setGraphic(textField);
            }

            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (isEditing()) {
                    textField.setText(tag.getNameProperty());
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                    if (empty) {
                        setText(null);             // handle id null, not accept it
                    } else {
                        setText(tag.getNameProperty());
                    }
                }

            }

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem().getNameProperty());
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                textField.requestFocus();
                textField.selectAll();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem().getNameProperty());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }

        getListview_tags().setEditable(true);
        getListview_tags().setCellFactory(lvTag -> new TagListCell());


    }

    private void fillPanel(MotherTask task)
    {

        txtfield_edit_name.setText(task.getNameProperty());
        if( task.getConstructor() != null) cbb_constructeur.setValue(task.getConstructor().getName());
        System.out.println("la nature de la tache mere est: " + task.getNature());
        if( task.getNature() != null) cbb_nature.setValue(task.getNature().getName());
        listview_edit_taches_filles.getItems().clear();
        listview_edit_taches_filles.setItems(task.getSubTaskList());
        cbb_tache_fille_1.getItems().clear();
        System.out.println("la liste de la subtasklist de la tache mere est de longueur: " + task.getSubTaskList().size());
        cbb_tache_fille_1.setItems(task.getSubTaskList());
        cbb_tache_fille_2.getItems().clear();
        cbb_tache_fille_2.setItems(task.getSubTaskList());
        fillConditions(task);
    }

    private void fillConditions(Task task)
    {
        listview_edit_conditions.getItems().clear();
        listview_edit_conditions.setItems(task.getConditionList());
    }

    private void fillPanel(LeafTask task)
    {
        txtfield_edit_name.setText(task.getNameProperty());
        fillConditions(task);
    }
    private void handleAddTag(){
        System.out.println("ajout tag");
        tags.addTag();

    }

    private void handleDeleteTag(){
        Tag currentTag = (Tag) getListview_tags().getSelectionModel().getSelectedItem();

        tags.removeTag(currentTag);

        //handle removal of tags from all concerned tasks
    }

    private void handleLayoutAction(ActionEvent event, OrganicLayout layout) {
        Button layoutButton = (Button) event.getSource();
        layoutButton.setDisable(true);

        graphControl.morphLayout(layout, Duration.ofMillis(500),
                // re-enable the action/button after everything has finished
                (source, args) -> layoutButton.setDisable(false));
    }

    public void handleOpenAction() {
        ICommand.OPEN.execute(null, graphControl);
    }

    public void handleSaveAction() {
        ICommand.SAVE.execute(null, graphControl);
    }

    private void graphFitContent() {
        System.out.println("on centre le graphique");
        graphControl.fitContent();
    }

    private void addNodeFromTask(LeafTask task){
        System.out.println("lancement de la methode d'ajout d'un node a partir d'une tache");
        INode node1 = graphControl.getGraph().createNode(new PointD(),TaskStyle,task);
        nodes.addNode(node1);
        graphControl.getGraph().addLabel(node1, task.getNameProperty());
    }


    private void addNodeFromTask(MotherTask task){
        System.out.println("lancement de la methode d'ajout d'un node a partir d'une tache");
        INode node1 = graphControl.getGraph().createNode(new PointD(),MotherTaskStyle,task);
        nodes.addNode(node1);
        graphControl.getGraph().addLabel(node1, task.getNameProperty());
    }

    private void changePanelState(boolean hasCurrentNode){
        if(!hasCurrentNode){
            // si il est actif on le cache
            System.out.println("Hide edit panel");
            getSplitPane_graph_edit().setDividerPositions(0.95);
        }else{
            // autrement on le montre
            System.out.println("Show edit panel");
            getSplitPane_graph_edit().setDividerPositions(0.63);
        }
        //panelActif = !panelActif;
    }

    // méthode pour mettre en place la suppression de la tache et du noeud séléctionné
    private void handleSuppressionNoeud(){
        if(currentNode!= null){
            System.out.println("Suppression du noeud " + currentNode.getTag());
            if(currentNode.getTag().getClass() == MotherTask.class){
                // si c'est une tache mere
                MotherTask task = (MotherTask) currentNode.getTag();
                motherTasks.removeTask(task);
            }else if (currentNode.getTag().getClass() == LeafTask.class){
                // si c'est une tache feuille
                LeafTask task = (LeafTask) currentNode.getTag();
                leafTasks.removeTask(task);
            }else{
                // si c'est une tache
                Task task = (Task) currentNode.getTag();
                tasks.removeTask(task);
            }
            graphControl.getGraph().remove(currentNode);
        }
    }

    // generation du graphique a partir d'une liste de taches
    private void createGraphFromTasks(MotherTasks motherTasks, Tasks tasks, LeafTasks leafTasks){
        // creation des noeuds
        createNodesFromTasks(motherTasks,tasks,leafTasks);
        // creation des liens
        createEdgeBetweenNodes();
        // mise en place du layout
        graphControl.morphLayout(hierarchicLayout, Duration.ofMillis(500));
        // mise a jour des styles
        updateNodeStyle();
    }

    // méthode pour creer tous les noeuds à partir des taches
    private void createNodesFromTasks(MotherTasks motherTasks, Tasks tasks, LeafTasks leafTasks){
        System.out.println("lancement de la methode de création des nodes à partir d'une liste de tache");
        // re-initialisation des nodes
        nodes = new Nodes();
        System.out.println("la longueur de mothertasks est: " + motherTasks.getTasks().size());
        System.out.println("la longueur de tasks est: " + tasks.getTasks().size());
        System.out.println("la longueur de leaftasks est: " + leafTasks.getTasks().size());
        // on parcours les taches mère
        for (MotherTask motherTask:motherTasks.getTasks()){
            // création d'un nouveau noeud avec la la motherTask comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + motherTask.getNameProperty()+ " " +motherTask.getIdProperty());
            INode node = graphControl.getGraph().createNode(new PointD(0,0),MotherTaskStyle,motherTask);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + motherTask.getNameProperty()+ " au noeud " + node.toString());
            graphControl.getGraph().addLabel(node, motherTask.getNameProperty());
        }
        // on parcours les taches
        for (Task task:tasks.getTasks()){
            // création d'un nouveau noeud avec la task comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + task.getNameProperty()+ " " +task.getIdProperty());
            INode node = graphControl.getGraph().createNode(new PointD(0,0),TaskStyle,task);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + task.getNameProperty()+ " au noeud " + node.toString());
            graphControl.getGraph().addLabel(node, task.getNameProperty());
        }

        // on parcours les taches mère filles
        for (LeafTask leafTask:leafTasks.getTasks()){
            // création d'un nouveau noeud avec la task comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + leafTask.getNameProperty()+ " " +leafTask.getIdProperty());
            INode node = graphControl.getGraph().createNode(new PointD(0,0),LeafTaskStyle,leafTask);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + leafTask.getNameProperty()+ " au noeud " + node.toString());
            graphControl.getGraph().addLabel(node, leafTask.getNameProperty());
        }

    }

    // méthode pour creer les liens entres tous les noeuds d'un graphes
    private void createEdgeBetweenNodes(){
        System.out.println("lancement de la methode de création des liens entre nodes à partir d'une liste de noeuds");

        // pour chaque noeuds
        System.out.println("la liste des noeuds est de longueur: "+ nodes.getNodes().size());

        for (INode node:nodes.getNodes()){
            // on récupère la tâche derrière le noeud
            if (node.getTag().getClass() == MotherTask.class) {
                MotherTask task = (MotherTask) node.getTag();
                System.out.println("la tache: "+ task.getNameProperty() + " est une tache mère, la longueur de ses sous taches est: " + task.getSubTaskList().size());
                // pour toutes les sous tâches
                List<INode> subtask = new ArrayList<>();
                boolean addLabel = false;
                for (String subTaskStringId : task.getSubTaskList()) {
                    System.out.println("la sous tache a l'id "+ subTaskStringId + " et de longueur " + subTaskStringId.length() );
                    // on vérifie tous les autres noeuds savoir s'il y en a un qui doit être lié
                    for (INode otherNode : nodes.getNodes()) {
                        // si on est pas sur le noeud courrant
                        if (otherNode != node) {
                            // si l'autre noeud est une tache mère
                            //System.out.println("on est sur un autre noeud" );
                            if (otherNode.getTag().getClass() == MotherTask.class) {
                                MotherTask otherTask = (MotherTask) otherNode.getTag();
                                // si l'id de la tache est celui d'une des sous tâches alors
                                System.out.println("on est sur un autre noeud MOTHER, d'id: " + otherTask.getIdProperty());
                                if (otherTask.getIdProperty().equals(subTaskStringId)) {
                                    System.out.println("on a " + otherTask.getIdProperty() + " == " + subTaskStringId + " et de longueur " + otherTask.getIdProperty().length());
                                    // création du lien graphique
                                    subtask.add(otherNode);
                                    IEdge edge = graphControl.getGraph().createEdge(node, otherNode);
                                    if(!addLabel) {
                                        graphControl.getGraph().addLabel(edge,task.getConstructor().getName());
                                        addLabel = true;
                                    }
                                    System.out.println("edge");
                                }
                            } else if (otherNode.getTag().getClass() == LeafTask.class) {
                                // si l'autre noeud est une tache fille
                                LeafTask otherTask = (LeafTask) otherNode.getTag();
                                System.out.println("on est sur un autre noeud LEAF, d'id: " + otherTask.getIdProperty() + " et de longueur " + otherTask.getIdProperty().length());
                                // si l'id de la tache est celui d'une des sous tâches alors
                                if (otherTask.getIdProperty().equals(subTaskStringId)) {
                                    System.out.println("on a " + otherTask.getIdProperty() + " == " + subTaskStringId );
                                    // création du lien graphique
                                    subtask.add(otherNode);
                                    IEdge edge = graphControl.getGraph().createEdge(node, otherNode);
                                    //graphControl.getGraph().addLabel(edge,task.getConstructor().getName());
                                    if(!addLabel) {
                                        graphControl.getGraph().addLabel(edge,task.getConstructor().getName());
                                        addLabel = true;
                                    }
                                    System.out.println("edge");
                                }
                            } else {
                                // si l'autre noeud est une tache
                                Task otherTask = (Task) otherNode.getTag();
                                System.out.println("on est sur un autre noeud TASK, d'id: " + otherTask.getIdProperty());
                                // si l'id de la tache est celui d'une des sous tâches alors
                                if (otherTask.getIdProperty().equals(subTaskStringId)) {
                                    System.out.println("on a " + otherTask.getIdProperty() + " == " + subTaskStringId);
                                    // création du lien graphique
                                    subtask.add(otherNode);
                                    graphControl.getGraph().createEdge(node, otherNode);
                                    System.out.println("edge");
                                }
                            }
                        }
                    }
                }
                //graph.groupNodes(node,subtask);
            }
        }
        System.out.println("Fin de la methode de création des liens entre nodes à partir d'une liste de noeuds");
    }

    // méthode pour changer et mettre a jour tous les labels
    public void setLabelToNodes(IGraph graph, Nodes nodes){
        System.out.println("lancement de la methode d'ajout des labels aux noeuds");
        // pour tous les noeuds
        for(INode node:nodes.getNodes()) {
            // on récupère la tâche derrière le noeud
            Task task = (Task) node.getTag();
            // on affecte au noeud son nom
            graph.addLabel(node, task.getNameProperty());
        }
        System.out.println("Fin de la methode d'ajout des labels aux noeuds");
    }

    // methode pour creer un lien entre deux taches sur les noeuds
    private void createLinkBetweenTwoNodes(IGraph graph, INode Mother, INode Daugther, IEdge egde){
        System.out.println("appel de la methode creatLinkBetweenTwoNodes");
        MotherTask motherTask;
        if(Mother.getTag().getClass() == Task.class){
            // si la tache mère était pour le moment une tache
            motherTask = new MotherTask((Task)Mother.getTag());
        }else if (Mother.getTag().getClass() == LeafTask.class){
            // si la mere était une leaf
            motherTask = new MotherTask((LeafTask) Mother.getTag());
        }else{
            // si la mere etait deja mere
            motherTask = (MotherTask) Mother.getTag();
        }
        // on sauvegarde derrière l'ancien noeud Mother le fait que c'est maintenant une mother task
        Mother.setTag(motherTask);
        // update du style
        graphControl.getGraph().setStyle(Mother,MotherTaskStyle);

        // on essaye de trouver le type de la classe fille
        if(Daugther.getTag().getClass() == MotherTask.class){
            System.out.println("la tache fille est une tache mere");
            // si la fille est une mother task
            MotherTask daugthertask = (MotherTask) Daugther.getTag();
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugthertask.getIdProperty());
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());

        }else if (Daugther.getTag().getClass() == LeafTask.class){
            System.out.println("la tache fille est une tache feuille");
            Task daugthertask = (Task) Daugther.getTag();
            LeafTask daugtherleaftask = new LeafTask(daugthertask);
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugtherleaftask.getIdProperty());
            graphControl.getGraph().setStyle(Daugther,LeafTaskStyle);
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());

        }else {
            System.out.println("la tache fille est une tache");
            // si la fille est une tache
            Task daugthertask = (Task) Daugther.getTag();
            //suppression de la tache a la liste des taches
            tasks.removeTask(daugthertask);
            // changement de la tache comme leaf tache
            LeafTask daugtherleaftask = new LeafTask(daugthertask);
            System.out.println("modification de la tache de Tache -> leafTask");
            // sauvegarde de la mère
            daugtherleaftask.setMother(motherTask);
            System.out.println("Sauvegarde par la leaf task de la tâche mere");
            // ajout de la leaf tache 
            // on sauvegarde derrière l'ancien noeud Daughet le fait que c'est maintenant une leaf task
            Daugther.setTag(motherTask);
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugtherleaftask.getIdProperty());
            System.out.println("ajout a la tache mere de l'id de la tache fille");
            // update du style
            graphControl.getGraph().setStyle(Daugther,LeafTaskStyle);
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());
        }

        // ajout du label du constructeur sur le lien si il y en a un
        if(motherTask.getConstructor() != null) graph.addLabel(egde,motherTask.getConstructor().getName());
    }


    private void updateNodeStyle(){
        System.out.println("lancement de la methode d'update des styles entre nodes à partir d'une liste de noeuds");
        // iterating over nodes
        for (INode node : graphControl.getGraph().getNodes()) {
            // do something with the node
            // si le noeud est une tache mère
            ShapeNodeStyle style;
            if( node.getTag().getClass() == MotherTask.class){
                style = MotherTaskStyle;
            }else if (node.getTag().getClass() == LeafTask.class){
                style = LeafTaskStyle;
            }else {
                style = TaskStyle;
            }
            graphControl.getGraph().setStyle(node,style);
        }
        System.out.println("Fin de la methode d'update des styles entre nodes à partir d'une liste de noeuds");
    }

    /*
    Création de style pour les différents noeuds. Ces styles ne sont pas définitifs et peuvent être amenés à évoluer.
    Juste mise en place du principe
    */
    private ShapeNodeStyle createRootTaskStyle(){
        // create a style which draws a node as a geometric shape with a fill and a border color
        ShapeNodeStyle rootTaskNodeStyle = new ShapeNodeStyle();
        rootTaskNodeStyle.setShape(ShapeNodeShape.ELLIPSE);
        rootTaskNodeStyle.setPaint(Color.color(0.1216, 0.4784, 0.549));
        rootTaskNodeStyle.setPen(Pen.getTransparent());
        return rootTaskNodeStyle;
    }

    private ShapeNodeStyle createMotherTaskStyle(){
        // create a style which draws a node as a geometric shape with a fill and a border color
        ShapeNodeStyle motherTaskNodeStyle = new ShapeNodeStyle();
        motherTaskNodeStyle.setShape(ShapeNodeShape.ELLIPSE);
        motherTaskNodeStyle.setPaint(Color.color(0.9882, 0.6902, 0.4941));
        motherTaskNodeStyle.setPen(Pen.getTransparent());
        return motherTaskNodeStyle;
    }

    private ShapeNodeStyle createTaskStyle(){
        ShapeNodeStyle taskNodeStyle = new ShapeNodeStyle();
        taskNodeStyle.setPaint(Color.color(0.749, 0.8588, 0.9686));
        taskNodeStyle.setPen(Pen.getTransparent());
        taskNodeStyle.setShape(ShapeNodeShape.ELLIPSE);
        return taskNodeStyle;
    }

    private ShapeNodeStyle createLeafStyle(){
        ShapeNodeStyle leafNodeStyle = new ShapeNodeStyle();
        leafNodeStyle.setPaint(Color.color(0.7255, 0.8078, 0.6784));
        leafNodeStyle.setPen(Pen.getTransparent());
        leafNodeStyle.setShape(ShapeNodeShape.ELLIPSE);
        return leafNodeStyle;
    }


    // méthode appelée par l'application une fois que le stage a été chargé.
    public void onLoaded() {
        // ajout du listener sur le bouton d'ajout du graph pour ouvrir une pop-up d'ajout
        button_graph_ajouter.setOnAction(e -> {
            PopupController wc = new PopupController();
            wc.showPopUp();
            if(wc.isMotherTask())
            {
                this.createdMotherTask = wc.getDataMotherTask();
                System.out.println("created task: " + getCreateMotherTask().getNameProperty());
                addNodeFromTask(createdMotherTask);
                motherTasks.addTask(createdMotherTask);
            }
            else
            {
                this.createdLeafTask = wc.getDataLeafTask();
                System.out.println("created task: " + getCreatedLeafTask().getNameProperty());
                addNodeFromTask(createdLeafTask);
                leafTasks.addTask(createdLeafTask);
            }

        });
        //fit graph after all element been loaded
        graphControl.fitGraphBounds();
    }

    // getter sur la tache créée
    private Task getCreatedLeafTask() {
        return createdLeafTask;
    }

    // getter sur la mother task créée
    private MotherTask getCreateMotherTask() {
        return createdMotherTask;
    }

    // classe pour gérer la pop up d'ajout de tâche
    class PopupController {
        private LeafTask leafTask;
        private MotherTask motherTask;

        private PopupController(){}

        private void showPopUp() {
            Stage popupwindow = new Stage();
            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("Ajout d'une tâche");

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            javafx.scene.text.Text scenetitle = new Text("Ajout d'une tache");
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 2, 1);

            Label taskID = new Label("Id:");
            grid.add(taskID, 0, 1);

            TextField idTextField = new TextField();
            grid.add(idTextField, 1, 1);

            Label name = new Label("Nom:");
            grid.add(name, 0, 2);

            TextField nameTextField = new TextField();
            grid.add(nameTextField, 1, 2);

            Label tacheMere = new Label("Creation d'une tâche mère :");
            grid.add(tacheMere, 0, 3);

            // creation de la checkbox pour la construction d'une tache mère
            CheckBox mereCheckBox = new CheckBox();
            HBox hboxmere = new HBox(mereCheckBox);
            hboxmere.setAlignment(Pos.CENTER_LEFT);
            grid.add(hboxmere, 1, 3);

            Label pop_label_constructeur = new Label("Constructeur :");
            grid.add(pop_label_constructeur, 0, 4);
            // Choix du constructeur
            ComboBox<String> pop_cbb_constructeur = new ComboBox<String>();
            pop_cbb_constructeur.setTooltip(new Tooltip("Constructeur"));
            pop_cbb_constructeur.getItems().addAll("IND","SEQ","SEQ_ORD","PAR","PAR_SIM","PAR_START","PAR_END");
            pop_cbb_constructeur.setValue("IND");
            grid.add(pop_cbb_constructeur, 1, 4);

            Label pop_label_nature = new Label("Nature :");
            grid.add(pop_label_nature, 0, 5);
            // Choix du constructeur
            ComboBox<String> pop_cbb_nature = new ComboBox<String>();
            pop_cbb_nature.setTooltip(new Tooltip("Nature"));
            pop_cbb_nature.getItems().addAll("INTERRUPTIBLE", "OPTIONELLE","ITERATIVE");
            pop_cbb_nature.setValue("INTERRUPTIBLE");
            grid.add(pop_cbb_nature, 1, 5);


            /*pop_label_constructeur.setVisible(false);
            pop_cbb_constructeur.setVisible(false);
            pop_label_nature.setVisible(false);
            pop_cbb_nature.setVisible(false);*/
            pop_label_constructeur.setDisable(true);
            pop_cbb_constructeur.setDisable(true);
            pop_label_nature.setDisable(true);
            pop_cbb_nature.setDisable(true);


            mereCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    /*pop_cbb_constructeur.setVisible(newValue);
                    pop_label_constructeur.setVisible(newValue);
                    pop_cbb_nature.setVisible(newValue);
                    pop_label_nature.setVisible(newValue);*/
                    pop_label_constructeur.setDisable(!newValue);
                    pop_cbb_constructeur.setDisable(!newValue);
                    pop_label_nature.setDisable(!newValue);
                    pop_cbb_nature.setDisable(!newValue);
                }
            });

            Button btn = new Button("Créer");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            grid.add(hbBtn, 1, 6);

            btn.setOnAction(e ->{
                        if (mereCheckBox.isSelected())
                        {
                            motherTask = new MotherTask();
                            if(!idTextField.getText().isEmpty()){
                                //System.out.println("On modifie la tache avec id: "+ idTextField.getText());
                                motherTask.setIdProperty(idTextField.getText());
                            }
                            if(!nameTextField.getText().isEmpty()){
                                //System.out.println("On modifie la tache avec nom: "+ nameTextField.getText());
                                motherTask.setNameProperty(nameTextField.getText());
                            }
                            motherTask.setConstructor(GlobalParameters.TypeConstructeur.valueOf(pop_cbb_constructeur.getValue()));
                            motherTask.setNature(GlobalParameters.Nature.valueOf(pop_cbb_nature.getValue()));
                            System.out.println("On créer une tache mere, sa nature est: "+ motherTask.getNature().getName());
                            leafTask = null;
                        }
                        else
                        {
                            leafTask = new LeafTask();
                            if(!idTextField.getText().isEmpty()){
                                //System.out.println("On modifie la tache avec id: "+ idTextField.getText());
                                leafTask.setIdProperty(idTextField.getText());
                            }
                            if(!nameTextField.getText().isEmpty()){
                                //System.out.println("On modifie la tache avec nom: "+ nameTextField.getText());
                                leafTask.setNameProperty(nameTextField.getText());
                            }
                            motherTask = null;
                        }
                        popupwindow.close();
                    }

            );

            Scene scene1 = new Scene(grid, 300, 250);

            popupwindow.setScene(scene1);
            popupwindow.showAndWait();
        }

        LeafTask getDataLeafTask() { return leafTask; }
        MotherTask getDataMotherTask() {
            return motherTask;
        }
        boolean isMotherTask(){  return (motherTask != null); }

    }

    /*
      All getters and setters
     */

    public Button getButton_save() {
        return button_save;
    }

    public void setButton_save(Button button_save) {
        this.button_save = button_save;
    }

    public Button getButton_undo() {
        return button_undo;
    }

    public void setButton_undo(Button button_undo) {
        this.button_undo = button_undo;
    }

    public Button getButton_redo() {
        return button_redo;
    }

    public void setButton_redo(Button button_redo) {
        this.button_redo = button_redo;
    }

    public Button getButton_centrer() {
        return button_centrer;
    }

    public void setButton_centrer(Button button_centrer) {
        this.button_centrer = button_centrer;
    }

    public Button getButton_hierarchiser() {
        return button_hierarchiser;
    }

    public void setButton_hierarchiser(Button button_hierarchiser) {
        this.button_hierarchiser = button_hierarchiser;
    }

    public Button getButton_graph_ajouter() {
        return button_graph_ajouter;
    }

    public void setButton_graph_ajouter(Button button_graph_ajouter) {
        this.button_graph_ajouter = button_graph_ajouter;
    }

    public Button getButton_graph_supprimer() {
        return button_graph_supprimer;
    }

    public void setButton_graph_supprimer(Button button_graph_supprimer) {
        this.button_graph_supprimer = button_graph_supprimer;
    }

    public VBox getVbox_tags() {
        return vbox_tags;
    }

    public void setVbox_tags(VBox vbox_tags) {
        this.vbox_tags = vbox_tags;
    }

    public Button getButton_xml_rafraichir() {
        return button_xml_rafraichir;
    }

    public void setButton_xml_rafraichir(Button button_xml_rafraichir) {
        this.button_xml_rafraichir = button_xml_rafraichir;
    }

    public Button getButton_edit_ajout_tache_fille() {
        return button_edit_ajout_tache_fille;
    }

    public void setButton_edit_ajout_tache_fille(Button button_edit_ajout_tache_fille) {
        this.button_edit_ajout_tache_fille = button_edit_ajout_tache_fille;
    }

    public Button getButton_edit_retrait_tache_fille() {
        return button_edit_retrait_tache_fille;
    }

    public void setButton_edit_retrait_tache_fille(Button button_edit_retrait_tache_fille) {
        this.button_edit_retrait_tache_fille = button_edit_retrait_tache_fille;
    }

    public Button getButton_edit_enregistrer_resume() {
        return button_edit_enregistrer_resume;
    }

    public void setButton_edit_enregistrer_resume(Button button_edit_enregistrer_resume) {
        this.button_edit_enregistrer_resume = button_edit_enregistrer_resume;
    }

    public TextField getTxtfield_edit_name() {
        return txtfield_edit_name;
    }

    public void setTxtfield_edit_name(TextField txtfield_edit_name) {
        this.txtfield_edit_name = txtfield_edit_name;
    }

    public ListView getListview_edit_taches_filles() {
        return listview_edit_taches_filles;
    }

    public void setListview_edit_taches_filles(ListView listview_edit_taches_filles) {
        this.listview_edit_taches_filles = listview_edit_taches_filles;
    }

    public ListView getListview_edit_autres_taches() {
        return listview_edit_autres_taches;
    }

    public void setListview_edit_autres_taches(ListView listview_edit_autres_taches) {
        this.listview_edit_autres_taches = listview_edit_autres_taches;
    }

    public Text getTxt_edit_id_resume() {
        return txt_edit_id_resume;
    }

    public void setTxt_edit_id_resume(Text txt_edit_id_resume) {
        this.txt_edit_id_resume = txt_edit_id_resume;
    }

    public Button getButton_edit_enregistrer_liens() {
        return button_edit_enregistrer_liens;
    }

    public void setButton_edit_enregistrer_liens(Button button_edit_enregistrer_liens) {
        this.button_edit_enregistrer_liens = button_edit_enregistrer_liens;
    }

    public Text getTxt_edit_id_liens() {
        return txt_edit_id_liens;
    }

    public void setTxt_edit_id_liens(Text txt_edit_id_liens) {
        this.txt_edit_id_liens = txt_edit_id_liens;
    }

    public Button getButton_edit_ajouter_conditions() {
        return button_edit_ajouter_conditions;
    }

    public void setButton_edit_ajouter_conditions(Button button_edit_ajouter_conditions) {
        this.button_edit_ajouter_conditions = button_edit_ajouter_conditions;
    }

    public Button getButton_edit_supprimer_conditions() {
        return button_edit_supprimer_conditions;
    }

    public void setButton_edit_supprimer_conditions(Button button_edit_supprimer_conditions) {
        this.button_edit_supprimer_conditions = button_edit_supprimer_conditions;
    }

    public Button getButton_edit_ajouter_assertions() {
        return button_edit_ajouter_assertions;
    }

    public void setButton_edit_ajouter_assertions(Button button_edit_ajouter_assertions) {
        this.button_edit_ajouter_assertions = button_edit_ajouter_assertions;
    }

    public Button getButton_edit_modifier_assertions() {
        return button_edit_modifier_assertions;
    }

    public void setButton_edit_modifier_assertions(Button button_edit_modifier_assertions) {
        this.button_edit_modifier_assertions = button_edit_modifier_assertions;
    }

    public Button getButton_edit_supprimer_assertions() {
        return button_edit_supprimer_assertions;
    }

    public void setButton_edit_supprimer_assertions(Button button_edit_supprimer_assertions) {
        this.button_edit_supprimer_assertions = button_edit_supprimer_assertions;
    }

    public Button getButton_edit_enregistrer_conditions() {
        return button_edit_enregistrer_conditions;
    }

    public void setButton_edit_enregistrer_conditions(Button button_edit_enregistrer_conditions) {
        this.button_edit_enregistrer_conditions = button_edit_enregistrer_conditions;
    }

    public Text getTxt_edit_nom_tache_conditions() {
        return txt_edit_nom_tache_conditions;
    }

    public void setTxt_edit_nom_tache_conditions(Text txt_edit_nom_tache_conditions) {
        this.txt_edit_nom_tache_conditions = txt_edit_nom_tache_conditions;
    }

    public Text getTxt_edit_condition_selectionne() {
        return txt_edit_condition_selectionne;
    }

    public void setTxt_edit_condition_selectionne(Text txt_edit_condition_selectionne) {
        this.txt_edit_condition_selectionne = txt_edit_condition_selectionne;
    }

    public ListView getListview_edit_conditions() {
        return listview_edit_conditions;
    }

    public void setListview_edit_conditions(ListView listview_edit_conditions) {
        this.listview_edit_conditions = listview_edit_conditions;
    }

    public ListView getListview_edit_assertions() {
        return listview_edit_assertions;
    }

    public void setListview_edit_assertions(ListView listview_edit_assertions) {
        this.listview_edit_assertions = listview_edit_assertions;
    }

    public SplitPane getSplitPane_graph_edit() {
        return splitPane_graph_edit;
    }

    public void setSplitPane_graph_edit(SplitPane splitPane_graph_edit) {
        this.splitPane_graph_edit = splitPane_graph_edit;
    }

    public Button getButton_graph_ajouter_tag() {
        return button_graph_ajouter_tag;
    }

    public void setButton_graph_ajouter_tag(Button button_graph_ajouter_tag) {
        this.button_graph_ajouter_tag = button_graph_ajouter_tag;
    }

    public Button getButton_graph_supprimer_tag() {
        return button_graph_supprimer_tag;
    }

    public void setButton_graph_supprimer_tag(Button button_graph_supprimer_tag) {
        this.button_graph_supprimer_tag = button_graph_supprimer_tag;
    }

    public ListView getListview_tags() {
        return listview_tags;
    }

    public void setListview_tags(ListView listview_tags) {
        this.listview_tags = listview_tags;
    }

    public Button getButton_open() {
        return button_open;
    }

    public void setButton_open(Button button_open) {
        this.button_open = button_open;
    }

    public TextField getPopup_textfield_id() {
        return popup_textfield_id;
    }

    public void setPopup_textfield_id(TextField popup_textfield_id) {
        this.popup_textfield_id = popup_textfield_id;
    }

    public TextField getPopup_textfield_name() {
        return popup_textfield_name;
    }

    public void setPopup_textfield_name(TextField popup_textfield_name) {
        this.popup_textfield_name = popup_textfield_name;
    }

    /////// FONCTION UTILISEE DANS LES LISTENERS
    public void save(){
        xmlFile.saveTextInFile();
    }

    public void refreshTreeFromXML() {
        System.out.println("Refresh button activated");
        save();
        System.out.println("saved!");
        xmlParser.createTasksFromXML(xmlFile.getXMLfilePath());
        createGraphFromTasks(motherTasks,tasks,leafTasks);
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        Stage newStage = new Stage();
        File file = fileChooser.showOpenDialog(newStage);
        System.out.println(file.toString());
        if (file != null) {
            tasks = new Tasks();
            xmlFile.setXMLfilePath(file.getPath());
            xmlFile.setTextFromFilePath();
            codeArea.replaceText(xmlFile.getXMLtext());
            xmlParser.createTasksFromXML(xmlFile.getXMLfilePath());
            tasks = xmlParser.getTasks();
            createGraphFromTasks(motherTasks,tasks,leafTasks);
        }
    }

    public void refreshXMLfromTree(){
        xmlParser.toXMLFromTree(motherTasks,leafTasks,xmlFile.getXMLfilePath());
        xmlFile.setTextFromFilePath();
        codeArea.replaceText(xmlFile.getXMLtext());
    }
}
