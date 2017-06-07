package start;

import Model.Tree.Tasks;
import Model.XML.XMLParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.fxmisc.richtext.demo.JavaKeywordsAsync;

/**
 * Created by ladyn-totorosaure on 25/05/17.
 */
public class TestXMLApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Controller/XMLEditorContainer.fxml"));
        Pane root = fxmlLoader.load();

        //get reference on the controller
        //ApplicationController controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(JavaKeywordsAsync.class.getResource("xml-highlighting.css").toExternalForm());
        stage.setTitle("Test vue XML");
        stage.setScene(scene);
        stage.show();
        Tasks tasks = new Tasks();
        XMLParser xmlparser = new XMLParser(tasks);
        xmlparser.createTasksFromXML("exempleXML.xml");

    }

    public static void main (String[] args){
        launch(args);
    }
}
