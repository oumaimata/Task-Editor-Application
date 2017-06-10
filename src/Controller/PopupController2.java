package Controller;

import Model.Tree.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;



public class PopupController {

    public static void display() {
        Stage popupwindow = new Stage();
        Task task = new Task();

        popupwindow.initModality(Modality.NONE);
        popupwindow.setTitle("This is a pop up window");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        javafx.scene.text.Text scenetitle = new Text("Ajout d'une tache");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label taskID = new Label("Id:");
        grid.add(taskID, 0, 1);

        TextField idTextField = new TextField();
        grid.add(idTextField, 1, 1);

        Label name = new Label("Password:");
        grid.add(name, 0, 2);

        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 2);

        Button btn = new Button("Créer");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene1 = new Scene(grid, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

}