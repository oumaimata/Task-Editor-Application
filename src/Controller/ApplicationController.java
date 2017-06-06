package Controller;

import Model.Tree.Task;
import Model.Tree.Tasks;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.tree.TreeLayout;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.ICommand;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

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
    public ViewController view;
    // reference sur l'ensemble des taches du modèle
    public Tasks tasks;
    // reference sur la tache selectionnée (dans le cas du graph notamment)
    public Task currentTask;

    public ApplicationController(ViewController view) {
        this.view = view;
    }

    public void initialize() {
        // ensemble des tâches à null initialement.
        // Dans le cas d'un chargement il faudra modifier directement cette valeur
        tasks = new Tasks();
    }

    // Méthode principale a utiliser dans le controller.
    // Y effectuer toutes les actions
    public void main_action (){
        // ceci est un exemple pour acceder a un element graphique
        Button test = view.getButton_graph_zoomin();
        test.setText("test test");

        // ajout du listener sur le bouton d'ajout de tache qui va déclancher l'ajout d'une tache par défaut
        view.getButton_graph_ajouter().setOnAction(evt -> ajoutTache());

        // ajout de la liste de toutes les tâches à la listview taches filles
        // ce n'est pas ce qu'il y aura dedans mais le fonctionnement est ok.
        // /!\ A adapter /!\
        view.getListview_edit_taches_filles().setItems(tasks.getTasks());
    }

    // Méthode pour réaliser les bindings des actions et des boutons
    public void make_binding (){

    }

    public void onLoaded() {

    }

    private ShapeNodeStyle createOrangeStyle(){
        // create a style which draws a node as a geometric shape with a fill and a border color
        ShapeNodeStyle orangeNodeStyle = new ShapeNodeStyle();
        orangeNodeStyle.setShape(ShapeNodeShape.RECTANGLE);
        orangeNodeStyle.setPaint(Color.ORANGE);
        orangeNodeStyle.setPen(Pen.getTransparent());
        return orangeNodeStyle;
    }

    private ShapeNodeStyle createBlueStyle(){
        ShapeNodeStyle blueNodeStyle = new ShapeNodeStyle();
        blueNodeStyle.setPaint(Color.CORNFLOWERBLUE);
        blueNodeStyle.setPen(Pen.getTransparent());
        return blueNodeStyle;
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
        graphControl.fitContent();
    }

    public void ajoutTache(){
        System.out.println("lancement de la methode ajout tache du controller");
        tasks.addDefaultTache();
    }


}
