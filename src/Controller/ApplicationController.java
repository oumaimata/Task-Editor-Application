package Controller;

import Model.Tree.Nodes;
import Model.Tree.Task;
import Model.Tree.Tasks;
import Model.XML.XMLFile;
import Model.XML.XMLParser;
import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.geometry.SizeD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.labelmodels.ExteriorLabelModel;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


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
    // reference sur la tache selectionnée (dans le cas du graph notamment)
    public Task currentTask;
    // reference sur la noeud selectionnée (dans le cas du graph notamment)
    public INode currentNode;
    // reference sur la liste des noeuds
    public Nodes nodes;
    // generated style
    ShapeNodeStyle MotherTaskStyle;
    ShapeNodeStyle TaskStyle;
    ShapeNodeStyle LeafTaskStyle;
    // edition du graph
    GraphEditorInputMode graphEditorInputMode;
    // savoir si le panel est relevé
    Boolean panelActif;
    // contient la chaine de caractére du XML
    public XMLFile xmlFile;
    // le parser pour le XML
    public XMLParser xmlParser;

    public ApplicationController(ViewController view, GraphControl graphControl) {
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
        MotherTaskStyle = createMotherTaskStyle();
        TaskStyle = createTaskStyle();
        LeafTaskStyle = createLeafStyle();
        // le panel est actif initialement
        panelActif = true;
        // initialisation de la tache courante
        currentTask = null;
        // initialisation du noeud courant
        currentNode = null;
        // initialisation du fichier XML
        xmlFile = new XMLFile();
        //initialisation du parser XML
        xmlParser = new XMLParser(tasks);
    }

    public void initialize() {
        // permettre l'edition directe du graph
        graphControl.setInputMode(graphEditorInputMode);
        // ensemble des tâches à null initialement.
        // Dans le cas d'un chargement il faudra modifier directement cette valeur
        tasks = new Tasks();
        // initialisation de la liste des noeuds
        nodes = new Nodes();
    }

    // Méthode principale a utiliser dans le controller.
    // Y effectuer toutes les actions
    public void main_action (){
        // ajout du listener sur le bouton d'ajout de tache qui va déclancher l'ajout d'une tache par défaut
        view.getButton_graph_ajouter().setOnAction(evt -> handleAjoutTache());

        // ajout de la liste de toutes les tâches à la listview taches filles
        // ce n'est pas ce qu'il y aura dedans mais le fonctionnement est ok.
        // /!\ A adapter /!\
        view.getListview_edit_taches_filles().setItems(tasks.getTasks());

        graphControl.currentItemProperty();
    }

    // Méthode pour réaliser les bindings des actions et des boutons
    public void make_binding (){

        // creation du listener sur le bouton pour centrer le graph
        view.getButton_centrer().setOnAction(evt -> graphFitContent());

        // ajout du listener sur le bouton de hierarchisation
        view.getButton_hierarchiser().setOnAction(evt -> handleLayoutAction(evt));

        // ajout du listener sur le bouton de hierarchisation
        view.getButton_graph_supprimer().setOnAction(evt -> handleSuppressionNoeud());

        // creation du listener sur le click d'un objet
        graphEditorInputMode.addItemLeftClickedListener(new IEventHandler<ItemClickedEventArgs<IModelItem>>() {
            @Override
            public void onEvent(Object o, ItemClickedEventArgs<IModelItem> iModelItemItemClickedEventArgs) {
                if(iModelItemItemClickedEventArgs.getItem().getTag().getClass() == Task.class){
                    System.out.println("l'objet sélectionné est un noeud de tache: " + iModelItemItemClickedEventArgs.getItem() );
                    // on récupère l'objet sélectionné
                    currentNode = (INode) iModelItemItemClickedEventArgs.getItem();
                    currentTask = (Task) currentNode.getTag();
                    System.out.println("l'objet courrant est devenu: " + currentTask );
                }
                // mise en place du panel d'édition
                System.out.println("Show edit panel");
                view.getSplitPane_graph_edit().setDividerPositions(0.63);
            }
        });

        // creation du listener sur le click d'une zone non clickable du graph : typiquement le fond du graph
        graphEditorInputMode.addCanvasClickedListener(new IEventHandler<ClickEventArgs>() {
            @Override
            public void onEvent(Object o, ClickEventArgs clickEventArgs) {
                // modification de l'etat du panel d'édition
                changePanelState();
                // on supprime la tache courante sélectionnée
                currentNode = null;
                currentTask = null;
            }
        });

    }

    public void handleLayoutAction(ActionEvent event) {
        Button layoutButton = (Button) event.getSource();
        layoutButton.setDisable(true);

        //tree layout (the one that we will use
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

    public void handleAjoutTache(){
        System.out.println("lancement de la methode ajout tache du controller & creation du noeud graphique avec comme tag la tache ainsi créée");
        INode node1 = graph.createNode(new PointD(),TaskStyle,tasks.addDefaultTache());
        nodes.addNode(node1);
        Task task = (Task) node1.getTag();
        graph.addLabel(node1, task.getNameProperty());
    }

    public void changePanelState(){
        if(panelActif){
            // si il est actif on le cache
            System.out.println("Hide edit panel");
            view.getSplitPane_graph_edit().setDividerPositions(0.95);
            panelActif = false;
        }else{
            // autrement on le montre
            System.out.println("Show edit panel");
            view.getSplitPane_graph_edit().setDividerPositions(0.63);
            panelActif = true;
        }
    }

    // méthode pour changer et mettre a jour tous les labels
    public void addLabelToNodes(){
        // pour tous les noeuds
        for(INode node:nodes.getNodes()) {
            // on récupère la tâche derrière le noeud
            Task task = (Task) node.getTag();
            // on affecte au noeud son nom
            graph.addLabel(node, task.getNameProperty());
        }
    }

    public void handleSuppressionNoeud(){
        if(currentNode!= null){
            System.out.println("Suppression du noeud " + currentTask.getIdProperty());
            graph.remove(currentNode);
        }
    }

    /*
    Création de style pour les différents noeuds. Ces styles ne sont pas définitifs et peuvent être amenés à évoluer.
    Juste mise en place du principe
    */
    private ShapeNodeStyle createMotherTaskStyle(){
        // create a style which draws a node as a geometric shape with a fill and a border color
        ShapeNodeStyle orangeNodeStyle = new ShapeNodeStyle();
        orangeNodeStyle.setShape(ShapeNodeShape.RECTANGLE);
        orangeNodeStyle.setPaint(Color.ORANGE);
        orangeNodeStyle.setPen(Pen.getTransparent());
        return orangeNodeStyle;
    }

    private ShapeNodeStyle createTaskStyle(){
        ShapeNodeStyle blueNodeStyle = new ShapeNodeStyle();
        blueNodeStyle.setPaint(Color.color(0.9882, 0.6902, 0.4941));
        blueNodeStyle.setPen(Pen.getTransparent());
        return blueNodeStyle;
    }

    private ShapeNodeStyle createLeafStyle(){
        ShapeNodeStyle blueNodeStyle = new ShapeNodeStyle();
        blueNodeStyle.setPaint(Color.CORNFLOWERBLUE);
        blueNodeStyle.setPen(Pen.getTransparent());
        return blueNodeStyle;
    }


}
