package View;

import Model.GlobalParameters;
import Model.Tree.LeafTask;
import Model.Tree.MotherTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Théo on 15/06/2017.
 */
public class PopupTaskCreation {
    private LeafTask leafTask;
    private MotherTask motherTask;

    public PopupTaskCreation(){}

    public LeafTask getDataLeafTask() { return leafTask; }
    public MotherTask getDataMotherTask() {
        return motherTask;
    }
    public boolean isMotherTask(){  return (motherTask != null); }

    public void showPopUp() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Ajout d'une tâche");

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

        Label name = new Label("Nom:");
        grid.add(name, 0, 2);

        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 2);

        Label tacheMere = new Label("Creation d'une tâche mère :");
        grid.add(tacheMere, 0, 3);

        // creation de la checkbox pour la construction d'une tache mère
        CheckBox mereCheckBox = new CheckBox();
        HBox hboxmere = new HBox(mereCheckBox);
        hboxmere.setAlignment(Pos.CENTER_LEFT);
        grid.add(hboxmere, 1, 3);

        Label pop_label_constructeur = new Label("Constructeur :");
        grid.add(pop_label_constructeur, 0, 4);
        // Choix du constructeur
        ComboBox<String> pop_cbb_constructeur = new ComboBox<String>();
        pop_cbb_constructeur.setTooltip(new Tooltip("Constructeur"));
        pop_cbb_constructeur.getItems().addAll("IND","SEQ","SEQ_ORD","PAR","PAR_SIM","PAR_START","PAR_END");
        pop_cbb_constructeur.setValue("IND");
        grid.add(pop_cbb_constructeur, 1, 4);

        Label pop_label_nature = new Label("Nature :");
        grid.add(pop_label_nature, 0, 5);
        // Choix du constructeur
        ComboBox<String> pop_cbb_nature = new ComboBox<String>();
        pop_cbb_nature.setTooltip(new Tooltip("Nature"));
        pop_cbb_nature.getItems().addAll("INTERRUPTIBLE", "OPTIONELLE","ITERATIVE");
        pop_cbb_nature.setValue("INTERRUPTIBLE");
        grid.add(pop_cbb_nature, 1, 5);

        pop_label_constructeur.setDisable(true);
        pop_cbb_constructeur.setDisable(true);
        pop_label_nature.setDisable(true);
        pop_cbb_nature.setDisable(true);


        mereCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                pop_label_constructeur.setDisable(!newValue);
                pop_cbb_constructeur.setDisable(!newValue);
                pop_label_nature.setDisable(!newValue);
                pop_cbb_nature.setDisable(!newValue);
            }
        });

        Button btn = new Button("Créer");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        btn.setOnAction(e ->{
                    if (mereCheckBox.isSelected())
                    {
                        motherTask = new MotherTask();
                        if(!idTextField.getText().isEmpty())
                            motherTask.setIdProperty(idTextField.getText());
                        if(!nameTextField.getText().isEmpty())
                            motherTask.setNameProperty(nameTextField.getText());

                        motherTask.setConstructor(GlobalParameters.TypeConstructeur.valueOf(pop_cbb_constructeur.getValue()));
                        motherTask.setNature(GlobalParameters.Nature.valueOf(pop_cbb_nature.getValue()));
                        leafTask = null;
                    }
                    else
                    {
                        leafTask = new LeafTask();
                        if(!idTextField.getText().isEmpty())
                            leafTask.setIdProperty(idTextField.getText());
                        if(!nameTextField.getText().isEmpty())
                            leafTask.setNameProperty(nameTextField.getText());
                        motherTask = null;
                    }
                    popupwindow.close();
                }
        );
        Scene scene1 = new Scene(grid, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}
