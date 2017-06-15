package Controller;

import Model.GlobalParameters;
import Model.Tree.*;
import Model.XML.XMLFile;
import Model.XML.XMLParser;
import View.PopupTaskCreation;
import View.XMLEditor;
import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.hierarchic.HierarchicLayout;
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

    public GraphControl graphControl = new GraphControl(); // reference sur le controller du graph

    private MotherTasks motherTasks = new MotherTasks(); // reference sur l'ensemble des taches meres du modèle
    private LeafTasks leafTasks = new LeafTasks(); // reference sur l'ensemble des taches feuilles du modèle

    private INode currentNode = null; // reference sur le noeud selectionné (dans le cas du graph notamment)
    private MotherTask currentMotherTask = null; // reference sur la tache lorsque c'est une tache mere
    private LeafTask currentLeafTask = null; // reference sur la tache lorsque c'est une tache feuille
    public Nodes nodes = new Nodes();; // reference sur la liste des noeuds

    public Tags tags = new Tags();; //reference sur la liste des tags

    // style des noeuds
    private ShapeNodeStyle MotherTaskStyle = createMotherTaskStyle();
    private ShapeNodeStyle LeafTaskStyle = createLeafStyle();

    private GraphEditorInputMode graphEditorInputMode = new GraphEditorInputMode(); // propriétés de l'édition de graphe
    private HierarchicLayout hierarchicLayout = new HierarchicLayout(); // layout utilisé pour l'arbre graphique

    private XMLFile xmlFile = new XMLFile(); // contient le texte du XML
    private XMLParser xmlParser = new XMLParser(); // le parser pour le XML
    private VirtualizedScrollPane vScrollPane;
    private CodeArea codeArea = new CodeArea();  // l'editeur

    @FXML private SplitPane splitPane_graph_edit; // Barre de sépration entre le graphe et le panel d'édition
    @FXML BorderPane borderPane; // border pane contenant l'éditeur
    @FXML private  Button button_save,button_undo,button_redo,button_centrer,button_hierarchiser, button_open; // Boutons bar de menu
    @FXML private  Button button_graph_ajouter,button_graph_supprimer, button_graph_ajouter_tag, button_graph_supprimer_tag; // Graph
    @FXML private VBox vbox_tags;
    @FXML private  ListView<Tag> listview_tags;
    @FXML private  Button button_xml_rafraichir;

    // Panel d'édition d'une tâche
    @FXML private TabPane panel;
    @FXML private Tab tab_resume,tab_liens,tab_condition;

    // Panel d'édition - Resume
    @FXML private  Button button_edit_ajout_tache_fille,button_edit_retrait_tache_fille,button_edit_enregistrer_resume;
    @FXML private  TextField txtfield_edit_name;
    @FXML private ComboBox<String> cbb_nature, cbb_constructeur;
    @FXML private  ListView<String> listview_edit_taches_filles,listview_edit_autres_taches;
    @FXML private  Text txt_edit_id_resume;
    @FXML private Text label_nature, label_constructeur,label_filles,label_autres;

    // Panel d'édition - Liens
    @FXML private  Button button_edit_enregistrer_liens;
    @FXML private ComboBox<String> cbb_tache_fille_1, cbb_tache_fille_2, cbb_lien_taches;
    @FXML private  Text txt_edit_id_liens;

    // Panel d'édition - Conditions
    @FXML private  Button button_edit_ajouter_conditions,button_edit_supprimer_conditions,button_edit_ajouter_assertions,button_edit_modifier_assertions,button_edit_supprimer_assertions,button_edit_enregistrer_conditions;
    @FXML private  Text txt_edit_nom_tache_conditions,txt_edit_condition_selectionne;
    @FXML private  ListView<Condition> listview_edit_conditions;
    @FXML private  ListView<Assertion> listview_edit_assertions;
    @FXML private ComboBox<String> cbb_type_condition, cbb_operateur_condition;

    public ApplicationController() {}

    public void initialize()
    {
        // Côté Graphe
        // mise en place de preference sur les objets des click : d'abord les noeuds, puis les fleches, puis les labels
        graphEditorInputMode.setClickHitTestOrder( new GraphItemTypes[]{ GraphItemTypes.NODE.or(GraphItemTypes.EDGE), GraphItemTypes.LABEL});
        graphEditorInputMode.setCreateNodeAllowed(false); // empecher la création d'un noeud au clic sur le graphique
        hierarchicLayout.setMinimumLayerDistance(50);
        hierarchicLayout.setNodeLabelConsiderationEnabled(true);
        listview_tags.setEditable(true);

        // Côté éditeur de code XML
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea)); // montrer les numeros de lignes
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, XMLEditor.computeHighlighting(newText));
            xmlFile.setXMLtext(newText);
        });
        codeArea.replaceText(0, 0, xmlFile.getXMLtext());
        vScrollPane = new VirtualizedScrollPane(codeArea);
        borderPane.setCenter(vScrollPane);

        // Côté Panel d'édition
        cbb_nature.getItems().addAll("interruptible", "optional","iterative");
        cbb_constructeur.getItems().addAll("IND","SEQ","SEQ_ORD","PAR","PAR_SIM","PAR_START","PAR_END");
        cbb_lien_taches.getItems().addAll(">","<","m","mi","o","oi","s","si","d","di","f","fi","=");
        cbb_type_condition.getItems().addAll("nomological","satisfaction","arret");
        cbb_operateur_condition.getItems().addAll("AND","OR","XOR","NOT");

        make_binding();
    }

    // Méthode pour réaliser les bindings des actions et des boutons
    private void make_binding (){
        // Boutons liées au XML
        button_open.setOnAction((event) -> openFile());
        button_save.setOnAction((event) -> save());
        button_xml_rafraichir.setOnAction((event) -> {refreshTreeFromXML();});
        xmlFile.XMLtextProperty().bindBidirectional(codeArea.accessibleTextProperty());

        // Boutons liés au graph
        button_centrer.setOnAction(evt -> graphFitContent());
        button_hierarchiser.setOnAction(evt -> handleLayoutAction(evt, hierarchicLayout));
        button_graph_supprimer.setOnAction(evt -> { handleSuppressionNoeud(); });

        // listener sur le click sur un objet du graph
        graphEditorInputMode.addItemLeftClickedListener(new IEventHandler<ItemClickedEventArgs<IModelItem>>() {
            @Override
            public void onEvent(Object o, ItemClickedEventArgs<IModelItem> iModelItemItemClickedEventArgs) {
                if(isTask(iModelItemItemClickedEventArgs.getItem().getTag()))
                {
                    currentNode = (INode) iModelItemItemClickedEventArgs.getItem(); // on récupère l'item sélectionné
                    if (currentNode.getTag().getClass() == MotherTask.class)
                    {
                        currentMotherTask = (MotherTask) currentNode.getTag(); // on récupère et on caste l'objet sélectionné
                        currentLeafTask=null;
                    }
                    else
                    {
                        currentLeafTask = (LeafTask) currentNode.getTag(); // on récupère et on caste l'objet sélectionné
                        currentMotherTask=null;
                    }

                    adjust_panel(currentMotherTask != null); // On met à jour le panel d'édition
                    changePanelState(true); // On lève le panel édition
                    if(currentMotherTask != null)
                        fillPanel(currentMotherTask);
                    else if (currentLeafTask != null)
                        fillPanel(currentLeafTask);
                }
            }
        });

        // Si on clique sur une zone du graphe sans noeud, on abaisse le panel d'édition
        graphEditorInputMode.addCanvasClickedListener(new IEventHandler<ClickEventArgs>() {
            @Override
            public void onEvent(Object o, ClickEventArgs clickEventArgs) { changePanelState(false); } // On descend le panel d'édition
        });

        // Si on clique sur une condition dans le panel, on affiche les assertions qui lui sont liées
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

        // Création des liens
        graphEditorInputMode.getCreateEdgeInputMode().setEdgeCreator((context, graph, sourceCandidate, targetCandidate, dummyEdge) -> {
            // get the source and target ports from the candidates
            IPort sourcePort= sourceCandidate.getPort();
            if (sourcePort == null) sourcePort = sourceCandidate.createPort(context);

            IPort targetPort = targetCandidate.getPort();
            if (targetPort == null) targetPort = targetCandidate.createPort(context);

            IEdge edge = graph.createEdge(sourcePort, targetPort, dummyEdge.getStyle());
            createLinkBetweenTwoNodes(edge.getSourceNode(),edge.getTargetNode(),edge);
            return edge;
        });
        graphEditorInputMode.getCreateEdgeInputMode().addEdgeCreatedListener(new IEventHandler<EdgeEventArgs>() {
            @Override
            public void onEvent(Object o, EdgeEventArgs edgeEventArgs) {}
        });
        graphControl.setInputMode(graphEditorInputMode);

        // listener sur tags
        button_graph_ajouter_tag.setOnAction(evt -> handleAddTag());
        button_graph_supprimer_tag.setOnAction(evt -> handleDeleteTag());

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
                if (isEditing())
                {
                    textField.setText(tag.getNameProperty());
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
                else
                {
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                    if (empty) setText(null);
                    else setText(tag.getNameProperty());
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
        listview_tags.setCellFactory(lvTag -> new TagListCell());
    }

    private boolean isTask(Object object) { return (object.getClass()==Task.class || object.getClass()==MotherTask.class || object.getClass()==LeafTask.class); }

    private void changePanelState(boolean hasCurrentNode)
    {
        if(hasCurrentNode)
            splitPane_graph_edit.setDividerPositions(0.63); // On lève le panel d'édition si une tâche est sélectionnée dans le graphe
        else
            splitPane_graph_edit.setDividerPositions(0.95); // On le baisse sinon
    }

    private void adjust_panel(boolean isMotherTask)
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

    private void fillPanel(MotherTask task)
    {
        txtfield_edit_name.setText(task.getNameProperty());
        if( task.getConstructor() != null) cbb_constructeur.setValue(task.getConstructor().getName());
        if( task.getNature() != null) cbb_nature.setValue(task.getNature().getName());
        listview_edit_taches_filles.getItems().clear();
        listview_edit_taches_filles.setItems(task.getSubTaskList());

        cbb_tache_fille_1.getItems().clear();
        cbb_tache_fille_1.setItems(task.getSubTaskList());
        cbb_tache_fille_2.getItems().clear();
        cbb_tache_fille_2.setItems(task.getSubTaskList());

        fillConditions(task);
    }

    private void fillPanel(LeafTask task)
    {
        txtfield_edit_name.setText(task.getNameProperty());
        fillConditions(task);
    }

    private void fillConditions(Task task)
    {
        txt_edit_nom_tache_conditions.setText(task.getNameProperty());
        listview_edit_conditions.setItems(task.getConditionList());
    }

    private void handleAddTag(){ tags.addTag(); }

    private void handleDeleteTag(){
        Tag currentTag = (Tag) listview_tags.getSelectionModel().getSelectedItem();
        tags.removeTag(currentTag);
    }

    private void handleLayoutAction(ActionEvent event, HierarchicLayout layout) {
        Button layoutButton = (Button) event.getSource();
        layoutButton.setDisable(true);
        graphControl.morphLayout(layout, Duration.ofMillis(500),
                (source, args) -> layoutButton.setDisable(false));
    }

    private void graphFitContent() { graphControl.fitContent(); }

    private void addNodeFromTask(LeafTask task){
        INode node1 = graphControl.getGraph().createNode(new PointD(),LeafTaskStyle,task);
        nodes.addNode(node1);
        graphControl.getGraph().addLabel(node1, task.getNameProperty());
    }


    private void addNodeFromTask(MotherTask task) {
        INode node1 = graphControl.getGraph().createNode(new PointD(),MotherTaskStyle,task);
        nodes.addNode(node1);
        graphControl.getGraph().addLabel(node1, task.getNameProperty());
    }

    // méthode pour mettre en place la suppression de la tache et du noeud séléctionné
    private void handleSuppressionNoeud(){
        if(currentNode!= null)
        {
            if(currentNode.getTag().getClass() == MotherTask.class){
                MotherTask task = (MotherTask) currentNode.getTag();
                motherTasks.removeTask(task);
            } else {
                LeafTask task = (LeafTask) currentNode.getTag();
                leafTasks.removeTask(task);
            }
            graphControl.getGraph().remove(currentNode);
        }
    }

    // generation du graphique a partir d'une liste de taches
    private void createGraphFromTasks(){
        createNodesFromTasks(); // creation des noeuds
        createEdgeBetweenNodes(); // creation des liens
        graphControl.morphLayout(hierarchicLayout, Duration.ofMillis(500)); // mise en place du layout
        updateNodeStyle(); // mise a jour des styles
    }

    // méthode pour creer tous les noeuds à partir des taches
    private void createNodesFromTasks(){
        nodes = new Nodes(); // re-initialisation des nodes
        for (MotherTask motherTask:motherTasks.getTasks())
        {
            INode node = graphControl.getGraph().createNode(new PointD(0,0),MotherTaskStyle,motherTask); // création d'un nouveau noeud avec la la motherTask comme tag
            nodes.addNode(node); // ajout du noeud à la liste des noeuds
            graphControl.getGraph().addLabel(node, motherTask.getNameProperty()); // ajout du label de la task au noeud
        }
        for (LeafTask leafTask:leafTasks.getTasks())
        {
            INode node = graphControl.getGraph().createNode(new PointD(0,0),LeafTaskStyle,leafTask); // création d'un nouveau noeud avec la task comme tag
            nodes.addNode(node); // ajout de ce noeuds a la liste des noeuds
            graphControl.getGraph().addLabel(node, leafTask.getNameProperty()); // ajout du label de la task au noeud
        }
    }

    // méthode pour creer les liens entres tous les noeuds d'un graphes
    private void createEdgeBetweenNodes()
    {
        for (INode node:nodes.getNodes())
        {
            if (node.getTag().getClass() == MotherTask.class) // on récupère la tâche derrière le noeud
            {
                MotherTask task = (MotherTask) node.getTag();
                boolean addLabel = true;
                for (String subTaskStringId : task.getSubTaskList())
                {
                    for (INode otherNode : nodes.getNodes())
                    {
                        Task otherTask = (Task) otherNode.getTag();
                        if (otherTask.getIdProperty().equals(subTaskStringId))
                        {
                            IEdge edge = graphControl.getGraph().createEdge(node, otherNode); // création du lien graphique
                            if(addLabel)  // On ajoute le constructeur une seule fois
                            {
                                graphControl.getGraph().addLabel(edge,task.getConstructor().getName());
                                addLabel = false;
                            }
                        }
                    }
                }
            }
        }
    }

    // methode pour creer un lien entre deux taches sur les noeuds
    private void createLinkBetweenTwoNodes(INode motherNode, INode daughterNode, IEdge egde){
        MotherTask motherTask;
        // Si le noeud mère référé une tâche non-mère, on la transforme en tâche mère
        if(motherNode.getTag().getClass() == Task.class || motherNode.getTag().getClass() == LeafTask.class)
            motherTask = new MotherTask((Task) motherNode.getTag());
        else
            motherTask = (MotherTask) motherNode.getTag();
        motherNode.setTag(motherTask); // on sauvegarde derrière l'ancien noeud Mother le fait que c'est maintenant une mother task
        graphControl.getGraph().setStyle(motherNode,MotherTaskStyle); // update du style
        boolean hasChildren = motherTask.getSubTaskList().size() != 0;

        Task daughterTask = (Task) daughterNode.getTag();
        motherTask.addSubTask(daughterTask.getIdProperty());

        // ajout du label du constructeur sur le lien si c'est la première fille
        if(!hasChildren && motherTask.getConstructor() != null)
            graphControl.getGraph().addLabel(egde,motherTask.getConstructor().getName());
    }

    private void updateNodeStyle(){
        for (INode node : graphControl.getGraph().getNodes())
        {
            if (node.getTag().getClass() == MotherTask.class)
                graphControl.getGraph().setStyle(node,MotherTaskStyle);
            else
                 graphControl.getGraph().setStyle(node,LeafTaskStyle);
        }
    }

    /*
    Création de style pour les différents noeuds. Ces styles ne sont pas définitifs et peuvent être amenés à évoluer.
    Juste mise en place du principe
    */
    private ShapeNodeStyle createMotherTaskStyle(){
        // create a style which draws a node as a geometric shape with a fill and a border color
        ShapeNodeStyle motherTaskNodeStyle = new ShapeNodeStyle();
        motherTaskNodeStyle.setShape(ShapeNodeShape.ELLIPSE);
        motherTaskNodeStyle.setPaint(Color.color(0.9882, 0.6902, 0.4941));
        motherTaskNodeStyle.setPen(Pen.getTransparent());
        return motherTaskNodeStyle;
    }

    private ShapeNodeStyle createLeafStyle(){
        ShapeNodeStyle leafNodeStyle = new ShapeNodeStyle();
        leafNodeStyle.setPaint(Color.color(0.7255, 0.8078, 0.6784));
        leafNodeStyle.setPen(Pen.getTransparent());
        leafNodeStyle.setShape(ShapeNodeShape.RECTANGLE);
        return leafNodeStyle;
    }


    // méthode appelée par l'application une fois que le stage a été chargé.
    public void onLoaded() {
        // ajout du listener sur le bouton d'ajout du graph pour ouvrir une pop-up d'ajout
        button_graph_ajouter.setOnAction(e -> {
            PopupTaskCreation wc = new PopupTaskCreation();
            wc.showPopUp();
            if(wc.isMotherTask())
            {
                addNodeFromTask(wc.getDataMotherTask());
                motherTasks.addTask(wc.getDataMotherTask());
                refreshXMLfromTree();
            }
            else
            {
                addNodeFromTask(wc.getDataLeafTask());
                leafTasks.addTask(wc.getDataLeafTask());
                refreshXMLfromTree();
            }
        });
        graphControl.fitGraphBounds();
    }

    public void save(){
        xmlFile.saveTextInFile();
    }

    public void refreshTreeFromXML()
    {
        save();
        graphControl.getGraph().clear();
        motherTasks = new MotherTasks();
        leafTasks = new LeafTasks();
        xmlParser.createTasksFromXML(xmlFile.getXMLfilePath());
        motherTasks = xmlParser.getMotherTasks();
        leafTasks = xmlParser.getLeafTasks();
        createGraphFromTasks();
    }

    private void openFile()
    {
        FileChooser fileChooser = new FileChooser();
        Stage newStage = new Stage();
        File file = fileChooser.showOpenDialog(newStage);
        System.out.println(file.toString());
        if (file != null) {
            graphControl.getGraph().clear();
            motherTasks = new MotherTasks();
            leafTasks = new LeafTasks();
            xmlFile.setXMLfilePath(file.getPath());
            xmlFile.setTextFromFilePath();
            codeArea.replaceText(xmlFile.getXMLtext());
            xmlParser.createTasksFromXML(xmlFile.getXMLfilePath());
            motherTasks = xmlParser.getMotherTasks();
            leafTasks = xmlParser.getLeafTasks();
            createGraphFromTasks();
        }
    }

    private void refreshXMLfromTree(){
        xmlParser.toXMLFromTree(motherTasks,leafTasks,xmlFile.getXMLfilePath());
        xmlFile.setTextFromFilePath();
        codeArea.replaceText(xmlFile.getXMLtext());
    }
}
