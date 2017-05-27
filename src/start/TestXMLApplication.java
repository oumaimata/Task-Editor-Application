package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by ladyn-totorosaure on 25/05/17.
 */
public class TestXMLApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/text_XML_view.fxml"));
        Pane root = (Pane) fxmlLoader.load();

        //get reference on the controller
        //ApplicationController controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("stylesheet.css");
        stage.setTitle("Test vue XML");
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args){
        launch(args);
    }
}
