package Controller;

import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.tree.TreeLayout;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.ICommand;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.time.Duration;


/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class ApplicationController {

    public GraphControl graphControl;
    private ShapeNodeStyle orangeNodeStyle = createOrangeStyle();
    private ShapeNodeStyle blueNodeStyle = createBlueStyle();

    public ViewController view;

    public ApplicationController(ViewController view) {
        this.view = view;
    }

    public void initialize() {

    }

    // Méthode principale a utiliser dans le controller.
    // Y effectuer toutes les actions
    public void main_action (){
        // ceci est un exemple pour acceder a un element graphique
        Button test = view.getButton_graph_zoomin();
        test.setText("test test");
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

}
