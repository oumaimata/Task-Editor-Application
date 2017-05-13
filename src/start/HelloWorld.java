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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelloWorld.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1365, 768);

        stage.setOnShown(windowEvent -> fxmlLoader.<HelloWorldController>getController().onLoaded());

        stage.setTitle("Hello, yFiles for JavaFX");
        stage.setScene(scene);
        stage.show();
    }
}