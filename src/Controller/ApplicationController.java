package Controller;

import Model.Tree.*;
import Model.XML.XMLFile;
import Model.XML.XMLParser;
import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.tree.TreeLayout;
import com.yworks.yfiles.utils.IEventHandler;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.ClickEventArgs;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import com.yworks.yfiles.view.input.ICommand;
import com.yworks.yfiles.view.input.ItemClickedEventArgs;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.time.Duration;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class ApplicationController {

    // reference sur le controller du graph
    public GraphControl graphControl;
    // reference sur le graph
    public IGraph graph;
    // reference sur le controller de la vue
    public ViewController view;
    // reference sur l'ensemble des taches du modèle
    public Tasks tasks;
    // reference sur l'ensemble des taches meres du modèle
    public MotherTasks motherTasks;
    // reference sur l'ensemble des taches feuilles du modèle
    public LeafTasks leafTasks;
    // reference sur le noeud selectionné (dans le cas du graph notamment)
    public INode currentNode;
    // reference sur la liste des noeuds
    public Nodes nodes;
    //reference sur la liste des tags
    public Tags tags;

    // generated style
    ShapeNodeStyle RootTaskStyle;
    ShapeNodeStyle MotherTaskStyle;
    ShapeNodeStyle TaskStyle;
    ShapeNodeStyle LeafTaskStyle;
    // edition du graph
    GraphEditorInputMode graphEditorInputMode;
    // savoir si le panel est relevé
    //Boolean panelActif;
    // contient la chaine de caractére du XML
    public XMLFile xmlFile;
    // le parser pour le XML
    public XMLParser xmlParser;
    // layout utilisé
    TreeLayout layout;

    public ApplicationController(ViewController view, GraphControl graphControl, ViewController.PopupController popup) {
        this.view = view;
        this.graphControl = graphControl;
        // obtenir la référence du graph
        this.graph = graphControl.getGraph();
        // reference sur l'edition du graph
        graphEditorInputMode = new GraphEditorInputMode();
        // mise en place de preference sur les objets des click : d'abord les noeuds, puis les fleches, puis les labels
        graphEditorInputMode.setClickHitTestOrder(
                new GraphItemTypes[]{
                        GraphItemTypes.NODE.or(GraphItemTypes.EDGE),
                        GraphItemTypes.LABEL});
        // empecher la création d'un noeud au clic sur le graphique
        graphEditorInputMode.setCreateNodeAllowed(false);
        // creation des styles
        RootTaskStyle = createRootTaskStyle();
        MotherTaskStyle = createMotherTaskStyle();
        TaskStyle = createTaskStyle();
        LeafTaskStyle = createLeafStyle();
        // le panel est actif initialement
        //panelActif = true;
        // initialisation du noeud courant
        currentNode = null;
        // initialisation du fichier XML
        xmlFile = new XMLFile();
        // initialisation du parser XML
        xmlParser = new XMLParser(tasks);
        // initialisation du layout utilisé
        layout = new TreeLayout();
    }

    public void initialize() {
        // permettre l'edition directe du graph
        graphControl.setInputMode(graphEditorInputMode);
        // ensemble des tâches à null initialement.
        // Dans le cas d'un chargement il faudra modifier directement cette valeur
        tasks = new Tasks();
        // initialisation de la liste des noeuds meres
        motherTasks = new MotherTasks();
        // initialisation de la liste des noeuds feuilles
        leafTasks = new LeafTasks();
        // initialisation de la liste des noeuds
        nodes = new Nodes();

        // on ajoute des taches pour test
        /*
        MotherTask task1 = motherTasks.addDefaultTache();
        MotherTask task2 = motherTasks.addDefaultTache();
        MotherTask task3 = motherTasks.addDefaultTache();
        LeafTask task4 = leafTasks.addDefaultTache();
        LeafTask task5 = leafTasks.addDefaultTache();

        // on creer des liens entre ces taches
        motherTasks.createLinkBetweenTwoTasks(task1,task2);
        motherTasks.createLinkBetweenTwoTasks(task2,task3);
        motherTasks.createLinkBetweenTwoTasks(task3,task4);
        motherTasks.createLinkBetweenTwoTasks(task3,task5);
        */
        // on creer le graph associé
        createGraphFromTasks(graph,motherTasks,tasks,leafTasks);

        //Creation de la liste des tags
        tags = new Tags();
    }

    // Méthode principale a utiliser dans le controller.
    // Y effectuer toutes les actions
    public void main_action (){
        // ajout de la liste de toutes les tâches à la listview taches filles
        // ce n'est pas ce qu'il y aura dedans mais le fonctionnement est ok.
        // /!\ A adapter /!\
        view.getListview_edit_taches_filles().setItems(tasks.getTasks());

        graphControl.currentItemProperty();

        view.getListview_tags().setItems(tags.getTags());

    }

    // Méthode pour réaliser les bindings des actions et des boutons

    public void make_binding (){
        // ajout du listener sur le bouton d'ajout de tache qui va déclancher l'ajout d'une tache par défaut
        //view.getButton_graph_ajouter().setOnAction(evt -> handleAjoutTache());


        // creation du listener sur le bouton pour centrer le graph
        view.getButton_centrer().setOnAction(evt -> graphFitContent());

        // ajout du listener sur le bouton de hierarchisation
        view.getButton_hierarchiser().setOnAction(evt -> handleLayoutAction(evt));

        // ajout du listener sur le bouton de hierarchisation
        view.getButton_graph_supprimer().setOnAction(evt -> {
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
                    view.currentTask = (Task) currentNode.getTag();
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
                    createLinkBetweenTwoNodes(edge.getSourceNode(),edge.getTargetNode());
                    // create a label
                    //graph.addLabel(edge, "A new edge");
                    // return the created edge
                    return edge;
                });
        graphEditorInputMode.getCreateEdgeInputMode().addEdgeCreatedListener(new IEventHandler<EdgeEventArgs>() {
            @Override
            public void onEvent(Object o, EdgeEventArgs edgeEventArgs) {

            }
        });
        // listener sur tags
        view.getButton_graph_ajouter_tag().setOnAction(evt -> handleAddTag());
        view.getButton_graph_supprimer_tag().setOnAction(evt -> handleDeleteTag());


//        ListChangeListener<Tag> tagsListener = changed -> {
//            if (changed.next()) {
//                if (changed.wasAdded()) {
//                    Tag newTag = changed.getAddedSubList().get(0);
//                    tags.addTag(newTag);
//
//                }
//
//            } else if (changed.wasRemoved()) {
//                Tag oldTag = changed.getRemoved().get(0);
//                tags.removeTag(oldTag);
//            }
//
//        };
//
//        tags.getTags().addListener(tagsListener);


        //Edit tags names
        class TagListCell extends ListCell<Tag> {
            private final TextField textField = new TextField();

            public TagListCell() {
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

 /*       class SimpleTagListCell extends ListCell<Tag> {

            @Override
            protected void updateItem(Tag item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                if (!empty && item != null) {
                    final String text = String.format("%s", item.getNameProperty());
                    setText(text);
                }
            }
        }

        view.getListview_tags().setCellFactory(lvTag -> new SimpleTagListCell());
*/
        view.getListview_tags().setEditable(true);
        view.getListview_tags().setCellFactory(lvTag -> new TagListCell());

    }

    public void handleAddTag(){
        System.out.println("ajout tag");
        tags.addTag();

    }

    public void handleDeleteTag(){
        Tag currentTag = (Tag) view.getListview_tags().getSelectionModel().getSelectedItem();

        tags.removeTag(currentTag);

        //handle removal of tags from all concerned tasks
    }

    public void handleLayoutAction(ActionEvent event) {
        Button layoutButton = (Button) event.getSource();
        layoutButton.setDisable(true);

        //tree layout (the one that we will use)
        TreeLayout layout = new TreeLayout();

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

    public void graphFitContent() {
        System.out.println("on centre le graphique");
        graphControl.fitContent();
    }

    /* --  N'est plus utile pour le moment --
    public void handleAjoutTache(){
        System.out.println("lancement de la methode ajout tache du controller & creation du noeud graphique avec comme tag la tache ainsi créée");
        INode node1 = graph.createNode(new PointD(),TaskStyle,tasks.addDefaultTache());
        nodes.addNode(node1);
        Task task = (Task) node1.getTag();
        graph.addLabel(node1, task.getNameProperty());
    }
    */

    public void addNodeFromTask(Task task){
        System.out.println("lancement de la methode d'ajout d'un node a partir d'une tache");
        INode node1 = graph.createNode(new PointD(),TaskStyle,task);
        nodes.addNode(node1);
        graph.addLabel(node1, task.getNameProperty());
    }


    public void addNodeFromTask(MotherTask task){
        System.out.println("lancement de la methode d'ajout d'un node a partir d'une tache");
        INode node1 = graph.createNode(new PointD(),MotherTaskStyle,task);
        nodes.addNode(node1);
        graph.addLabel(node1, task.getNameProperty());
    }

    public void changePanelState(boolean hasCurrentNode){
        if(!hasCurrentNode){
            // si il est actif on le cache
            System.out.println("Hide edit panel");
            view.getSplitPane_graph_edit().setDividerPositions(0.95);
        }else{
            // autrement on le montre
            System.out.println("Show edit panel");
            view.getSplitPane_graph_edit().setDividerPositions(0.63);
        }
        //panelActif = !panelActif;
    }

    // méthode pour mettre en place la suppression de la tache et du noeud séléctionné
    public void handleSuppressionNoeud(){
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
            graph.remove(currentNode);
        }
    }

    // generation du graphique a partir d'une liste de taches
    public void createGraphFromTasks(IGraph graph, MotherTasks motherTask, Tasks tasks, LeafTasks leafTasks){
        // creation des noeuds
        createNodesFromTasks(graph,motherTask,tasks,leafTasks);
        // creation des liens
        createEdgeBetweenNodes(graph);
        // mise en place du layout
        graphControl.morphLayout(layout, Duration.ofMillis(500));
        // mise a jour des styles
        updateNodeStyle(graph);
    }

    // méthode pour creer tous les noeuds à partir des taches
    private void createNodesFromTasks(IGraph graph, MotherTasks motherTasks, Tasks tasks, LeafTasks leafTasks){
        System.out.println("lancement de la methode de création des nodes à partir d'une liste de tache");
        // re-initialisation des nodes
        nodes = new Nodes();

        // on parcours les taches mère
        for (MotherTask motherTask:motherTasks.getTasks()){
            // création d'un nouveau noeud avec la la motherTask comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + motherTask.getNameProperty()+ " " +motherTask.getIdProperty());
            INode node = graph.createNode(new PointD(0,0),MotherTaskStyle,motherTask);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + motherTask.getNameProperty()+ " au noeud " + node.toString());
            graph.addLabel(node, motherTask.getNameProperty());
        }
        // on parcours les taches
        for (Task task:tasks.getTasks()){
            // création d'un nouveau noeud avec la task comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + task.getNameProperty()+ " " +task.getIdProperty());
            INode node = graph.createNode(new PointD(0,0),TaskStyle,task);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + task.getNameProperty()+ " au noeud " + node.toString());
            graph.addLabel(node, task.getNameProperty());
        }

        // on parcours les taches mère filles
        for (LeafTask leafTask:leafTasks.getTasks()){
            // création d'un nouveau noeud avec la task comme tag
            System.out.println("creation d'un nouveau node avec comme tag: " + leafTask.getNameProperty()+ " " +leafTask.getIdProperty());
            INode node = graph.createNode(new PointD(0,0),LeafTaskStyle,leafTask);
            // ajout de ce noeuds a la liste des noeuds
            nodes.addNode(node);
            // ajout du label de la task au noeud
            System.out.println("Ajout du label: " + leafTask.getNameProperty()+ " au noeud " + node.toString());
            graph.addLabel(node, leafTask.getNameProperty());
        }


        //System.out.println("Fin de la methode de création des nodes à partir d'une liste de tache");
    }

    // méthode pour creer les liens entres tous les noeuds d'un graphes
    private void createEdgeBetweenNodes(IGraph graph){
        System.out.println("lancement de la methode de création des liens entre nodes à partir d'une liste de noeuds");

        // pour chaque noeuds
        System.out.println("la liste des noeuds est de longueur: "+ nodes.getNodes().size());

        for (INode node:nodes.getNodes()){
            // on récupère la tâche derrière le noeud
            if (node.getTag().getClass() == MotherTask.class) {
                MotherTask task = (MotherTask) node.getTag();
                System.out.println("la tache: "+ task.getNameProperty() + " est une tache mère, la longueur de ses sous taches est: " + task.getSubTaskList().size());
                // pour toutes les sous tâches
                for (String subTaskStringId : task.getSubTaskList()) {
                    System.out.println("la sous tache a l'id "+ subTaskStringId );
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
                                if (otherTask.getIdProperty() == subTaskStringId) {
                                    // création du lien graphique
                                    graph.createEdge(node, otherNode);
                                }
                            } else if (otherNode.getTag().getClass() == LeafTask.class) {
                                // si l'autre noeud est une tache fille
                                LeafTask otherTask = (LeafTask) otherNode.getTag();
                                System.out.println("on est sur un autre noeud LEAF, d'id: " + otherTask.getIdProperty());
                                // si l'id de la tache est celui d'une des sous tâches alors
                                if (otherTask.getIdProperty() == subTaskStringId) {
                                    // création du lien graphique
                                    graph.createEdge(node, otherNode);
                                }
                            } else {
                                // si l'autre noeud est une tache
                                Task otherTask = (Task) otherNode.getTag();
                                System.out.println("on est sur un autre noeud TASK, d'id: " + otherTask.getIdProperty());
                                // si l'id de la tache est celui d'une des sous tâches alors
                                if (otherTask.getIdProperty() == subTaskStringId) {
                                    // création du lien graphique
                                    graph.createEdge(node, otherNode);
                                }
                            }
                        }
                    }
                }
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
    private void createLinkBetweenTwoNodes(INode Mother, INode Daugther){
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
        graph.setStyle(Mother,MotherTaskStyle);

        // on essaye de trouver le type de la classe fille
        if(Daugther.getTag().getClass() == MotherTask.class){
            // si la fille est une mother task
            MotherTask daugthertask = (MotherTask) Daugther.getTag();
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugthertask.getIdProperty());
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());

        }else if (Daugther.getTag().getClass() == LeafTask.class){
            // si la fille est une leaf tag
            LeafTask daugthertask = (LeafTask) Daugther.getTag();
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugthertask.getIdProperty());
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());

        }else {
            // si la fille est une tache
            Task daugthertask = (Task) Daugther.getTag();
            // changement de la tache comme leaf tache
            LeafTask daugtherleaftask = new LeafTask(daugthertask);
            // on sauvegarde derrière l'ancien noeud Daughet le fait que c'est maintenant une leaf task
            Daugther.setTag(motherTask);
            // ajout de cette tache a la liste des sous taches de la mère
            motherTask.addSubTask(daugtherleaftask.getIdProperty());
            // update du style
            graph.setStyle(Daugther,LeafTaskStyle);
            System.out.println("Creation d'un lien de parenté entre "+ motherTask.getIdProperty() + " et "+ daugthertask.getIdProperty());

        }
    }


    private void updateNodeStyle(IGraph graph){
        System.out.println("lancement de la methode d'update des styles entre nodes à partir d'une liste de noeuds");
        // iterating over nodes
        for (INode node : graph.getNodes()) {
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
            graph.setStyle(node,style);
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
}
