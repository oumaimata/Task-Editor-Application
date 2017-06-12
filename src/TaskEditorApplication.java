import Controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.richtext.demo.JavaKeywordsAsync;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class TaskEditorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./Task-editor-layout.fxml"));
        Parent root = fxmlLoader.load();
        stage.setOnShown(windowEvent -> fxmlLoader.<ViewController>getController().onLoaded());

        Scene scene = new Scene(root,1000, 800);
        scene.getStylesheets().add(JavaKeywordsAsync.class.getResource("xml-highlighting.css").toExternalForm());
        stage.setScene(scene);
        // Set full screen
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.show();
    }

    public TaskEditorApplication() {
    }
}