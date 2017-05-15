package start;

import com.yworks.yfiles.geometry.PointD;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.*;
import com.yworks.yfiles.graph.styles.ShapeNodeShape;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.layout.tree.TreeLayout;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.Pen;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import com.yworks.yfiles.view.input.ICommand;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.time.Duration;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class HelloWorldController {

    public GraphControl graphControl;
    private ShapeNodeStyle orangeNodeStyle = createOrangeStyle();
    private ShapeNodeStyle blueNodeStyle = createBlueStyle();

    public void initialize() {
        // Called by the JavaFX framework on loading.

        //input mode enable user interaction
        graphControl.setInputMode(new GraphEditorInputMode());

        //enabling loading and saving
        graphControl.setFileIOEnabled(true);

        //get graph reference
        IGraph graph = graphControl.getGraph();

        //create new default style for node
        graph.getNodeDefaults().setStyle(blueNodeStyle);

        //create 3 nodes
        //initialize node 1 with orange style
        INode node1 = graph.createNode(new RectD(0, 0, 30, 30), orangeNodeStyle);
        INode node2 = graph.createNode(new RectD(100, 0, 30, 30));
        INode node3 = graph.createNode(new RectD(300, 300, 60, 30));

        //set new style
        graph.setStyle(node2,orangeNodeStyle);

        //creating 2 edge between node1->node2 and node2->node3
        IEdge edge1 = graph.createEdge(node1, node2);
        IEdge edge2 = graph.createEdge(node2, node3);

        //add a bend
        IBend bend1 = graph.addBend(edge2, new PointD(330, 15));

        //adding some labels
        ILabel ln1 = graph.addLabel(node1, "n1");
        ILabel ln2 = graph.addLabel(node2, "n2");
        ILabel ln3 = graph.addLabel(node3, "n3");
        ILabel le3 = graph.addLabel(edge1, "edge1-2");
        ILabel le4 = graph.addLabel(edge2, "edge2-3");
    }

    public void onLoaded() {
        // Called by our application right after stage is loaded.
        //fit graph after all element been loaded
        graphControl.fitGraphBounds();
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
}
