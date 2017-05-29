package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class HelloWorld extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Task-editor-layout.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1366, 768);

        stage.setOnShown(windowEvent -> fxmlLoader.<HelloWorldController>getController().onLoaded());

        stage.setTitle("Task Editor");
        stage.setMaximized(false);
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
    }
}