package Controller;

import Model.Tree.MotherTask;
import Model.Tree.Task;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import View.XMLEditor;

import java.io.File;

/**
 * Created by pierrelouislacorte on 29/05/2017.
 */
public class ViewController {
    ApplicationController applicationController;
    Task createTask;
    MotherTask createMotherTask;

    public Task currentTask;

    public GraphControl graphControl;

    public ViewController()
    {
        currentTask = new Task();
    }

    // méthode appelée au moment ou on parse le FMXL et grâce a laquelle tout se construit
    public void initialize() {
        PopupController popup = new PopupController();
        // creation de l'application controller
        this.applicationController = new ApplicationController(this, graphControl, popup);
        // permettre l'edition directe du graph
        graphControl.setInputMode(new GraphEditorInputMode());
        applicationController.initialize();
        // on fait les bindings depuis applicationController
        applicationController.make_binding();
        // on déclanche la méthode d'action principale du controller de l'application
        applicationController.main_action();
        // initialisation de l'éditeur de code
        codeArea = new CodeArea();
        vScrollPane = new VirtualizedScrollPane(codeArea);
        borderPane.setCenter(vScrollPane);

        // montrer les numeros de lignes
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, XMLEditor.computeHighlighting(newText));
            applicationController.xmlFile.setXMLtext(newText);
        });
        codeArea.replaceText(0, 0, applicationController.xmlFile.getXMLtext());
        // setting the action to open a document
        button_open.setOnAction((event) -> {openFile();});
        button_save.setOnAction((event) -> {save();});
        button_xml_rafraichir.setOnAction((event) -> {refreshTreeFromXML();});
        applicationController.xmlFile.XMLtextProperty().bindBidirectional(codeArea.accessibleTextProperty());

        bindindTaskAndEdition();


    }

    public void bindindTaskAndEdition()
    {
        txtfield_edit_name.textProperty().bindBidirectional(currentTask.namePropertyProperty());
        txt_edit_id_resume.textProperty().bindBidirectional(currentTask.idPropertyProperty());
    }

    // méthode appelée par l'application une fois que le stage a été chargé.
    public void onLoaded() {
        // ajout du listener sur le bouton d'ajout du graph pour ouvrir une pop-up d'ajout
        getButton_graph_ajouter().setOnAction(e -> {
            PopupController wc = new PopupController();
            wc.showPopUp();
            if(wc.isMotherTask()){
                this.createMotherTask = wc.getDataMotherTask();
                System.out.println("created task: " + getCreateMotherTask().getNameProperty());
                applicationController.addNodeFromTask(wc.getDataMotherTask());
            }else{
                this.createTask = wc.getDataTask();
                System.out.println("created task: " + getCreatedTask().getNameProperty());
                applicationController.addNodeFromTask(wc.getDataTask());
            }



        });
        //fit graph after all element been loaded
        graphControl.fitGraphBounds();
    }

    // getter sur la tache créée
    public Task getCreatedTask() {
        return createTask;
    }

    // getter sur la mother task créée
    public MotherTask getCreateMotherTask() {
        return createMotherTask;
    }

    // classe pour gérer la pop up d'ajout de tâche
    class PopupController {
        private Task task;
        private MotherTask motherTaskPopup;

        public PopupController(){}

        public void showPopUp() {
            this.task = new Task();

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

            Label tacheMere = new Label("Creation d'une tâche mère:");
            grid.add(tacheMere, 0, 3);

            CheckBox mereCheckBox = new CheckBox();
            grid.add(mereCheckBox, 1, 3);

            Button btn = new Button("Créer");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            grid.add(hbBtn, 1, 5);

            btn.setOnAction(e ->{
                if(idTextField.getText()!=null)
                    task.setIdProperty(idTextField.getText());
                if(nameTextField.getText()!=null)
                    task.setNameProperty(nameTextField.getText());

                if(mereCheckBox.isSelected()){
                    // si la checkbox est selectionnée alors on créer une tâche mère directement
                    this.motherTaskPopup = new MotherTask(task);
                    this.task = null;
                }
                popupwindow.close();
            }

            );

            Scene scene1 = new Scene(grid, 300, 250);

            popupwindow.setScene(scene1);
            popupwindow.showAndWait();
        }

        Task getDataTask() {
            return task;
        }

        MotherTask getDataMotherTask() {
            return motherTaskPopup;
        }

        boolean isMotherTask(){
            if(getDataTask() == null){
                return true;
            }else{
                return false;
            }
        }

    }

    // le container de l'editeur XML

    VirtualizedScrollPane vScrollPane;
    // l'editeur
    CodeArea codeArea;
    // border pane contenant l'éditeur
    @FXML
    BorderPane borderPane;

    // bar de menu
    @FXML
    private  Button button_save,button_undo,button_redo,button_centrer,button_hierarchiser, button_open;
    // Graph
    @FXML
    private  Button button_graph_ajouter,button_graph_supprimer, button_graph_ajouter_tag, button_graph_supprimer_tag;
    @FXML
    private  VBox vbox_tags;

    // XML
    @FXML
    private  Button button_xml_rafraichir;

    // Panel d'édition - Resume
    @FXML
    private  Button button_edit_ajout_tache_fille,button_edit_retrait_tache_fille,button_edit_enregistrer_resume;
    @FXML
    private  TextField txtfield_edit_name;
    @FXML
    private  MenuButton menubutton_edit_nature,menubutton_edit_constructeur;
    @FXML
    private  ListView<Task> listview_edit_taches_filles,listview_edit_autres_taches;
    @FXML
    private  Text txt_edit_id_resume;

    // Panel d'édition - Liens
    @FXML
    private  Button button_edit_enregistrer_liens;
    @FXML
    private  MenuButton menubutton_edit_premiere_tache_fille,menubutton_edit_deuxieme_tache_fille,menubutton_edit_lien_entre_taches;
    @FXML
    private  Text txt_edit_id_liens;

    // Panel d'édition - Conditions
    @FXML
    private  Button button_edit_ajouter_conditions,button_edit_supprimer_conditions,button_edit_ajouter_assertions,button_edit_modifier_assertions,button_edit_supprimer_assertions,button_edit_enregistrer_conditions;
    @FXML
    private  Text txt_edit_nom_tache_conditions,txt_edit_condition_selectionne;
    @FXML
    private  ListView listview_edit_conditions,listview_edit_assertions;
    @FXML
    private  MenuButton menubutton_edit_type_condition,menubutton_edit_operateur_logique_condition;

    // Split pane
    @FXML
    private SplitPane splitPane_graph_edit;

    // test
    @FXML
    private TextField popup_textfield_id, popup_textfield_name;

    /*
      All getters and setters
     */

    public Button getButton_save() {
        return button_save;
    }

    public void setButton_save(Button button_save) {
        this.button_save = button_save;
    }

    public Button getButton_undo() {
        return button_undo;
    }

    public void setButton_undo(Button button_undo) {
        this.button_undo = button_undo;
    }

    public Button getButton_redo() {
        return button_redo;
    }

    public void setButton_redo(Button button_redo) {
        this.button_redo = button_redo;
    }

    public Button getButton_centrer() {
        return button_centrer;
    }

    public void setButton_centrer(Button button_centrer) {
        this.button_centrer = button_centrer;
    }

    public Button getButton_hierarchiser() {
        return button_hierarchiser;
    }

    public void setButton_hierarchiser(Button button_hierarchiser) {
        this.button_hierarchiser = button_hierarchiser;
    }

    public Button getButton_graph_ajouter() {
        return button_graph_ajouter;
    }

    public void setButton_graph_ajouter(Button button_graph_ajouter) {
        this.button_graph_ajouter = button_graph_ajouter;
    }

    public Button getButton_graph_supprimer() {
        return button_graph_supprimer;
    }

    public void setButton_graph_supprimer(Button button_graph_supprimer) {
        this.button_graph_supprimer = button_graph_supprimer;
    }

    public VBox getVbox_tags() {
        return vbox_tags;
    }

    public void setVbox_tags(VBox vbox_tags) {
        this.vbox_tags = vbox_tags;
    }

    public Button getButton_xml_rafraichir() {
        return button_xml_rafraichir;
    }

    public void setButton_xml_rafraichir(Button button_xml_rafraichir) {
        this.button_xml_rafraichir = button_xml_rafraichir;
    }

    public Button getButton_edit_ajout_tache_fille() {
        return button_edit_ajout_tache_fille;
    }

    public void setButton_edit_ajout_tache_fille(Button button_edit_ajout_tache_fille) {
        this.button_edit_ajout_tache_fille = button_edit_ajout_tache_fille;
    }

    public Button getButton_edit_retrait_tache_fille() {
        return button_edit_retrait_tache_fille;
    }

    public void setButton_edit_retrait_tache_fille(Button button_edit_retrait_tache_fille) {
        this.button_edit_retrait_tache_fille = button_edit_retrait_tache_fille;
    }

    public Button getButton_edit_enregistrer_resume() {
        return button_edit_enregistrer_resume;
    }

    public void setButton_edit_enregistrer_resume(Button button_edit_enregistrer_resume) {
        this.button_edit_enregistrer_resume = button_edit_enregistrer_resume;
    }

    public TextField getTxtfield_edit_name() {
        return txtfield_edit_name;
    }

    public void setTxtfield_edit_name(TextField txtfield_edit_name) {
        this.txtfield_edit_name = txtfield_edit_name;
    }

    public MenuButton getMenubutton_edit_nature() {
        return menubutton_edit_nature;
    }

    public void setMenubutton_edit_nature(MenuButton menubutton_edit_nature) {
        this.menubutton_edit_nature = menubutton_edit_nature;
    }

    public MenuButton getMenubutton_edit_constructeur() {
        return menubutton_edit_constructeur;
    }

    public void setMenubutton_edit_constructeur(MenuButton menubutton_edit_constructeur) {
        this.menubutton_edit_constructeur = menubutton_edit_constructeur;
    }

    public ListView getListview_edit_taches_filles() {
        return listview_edit_taches_filles;
    }

    public void setListview_edit_taches_filles(ListView listview_edit_taches_filles) {
        this.listview_edit_taches_filles = listview_edit_taches_filles;
    }

    public ListView getListview_edit_autres_taches() {
        return listview_edit_autres_taches;
    }

    public void setListview_edit_autres_taches(ListView listview_edit_autres_taches) {
        this.listview_edit_autres_taches = listview_edit_autres_taches;
    }

    public Text getTxt_edit_id_resume() {
        return txt_edit_id_resume;
    }

    public void setTxt_edit_id_resume(Text txt_edit_id_resume) {
        this.txt_edit_id_resume = txt_edit_id_resume;
    }

    public Button getButton_edit_enregistrer_liens() {
        return button_edit_enregistrer_liens;
    }

    public void setButton_edit_enregistrer_liens(Button button_edit_enregistrer_liens) {
        this.button_edit_enregistrer_liens = button_edit_enregistrer_liens;
    }

    public MenuButton getMenubutton_edit_premiere_tache_fille() {
        return menubutton_edit_premiere_tache_fille;
    }

    public void setMenubutton_edit_premiere_tache_fille(MenuButton menubutton_edit_premiere_tache_fille) {
        this.menubutton_edit_premiere_tache_fille = menubutton_edit_premiere_tache_fille;
    }

    public MenuButton getMenubutton_edit_deuxieme_tache_fille() {
        return menubutton_edit_deuxieme_tache_fille;
    }

    public void setMenubutton_edit_deuxieme_tache_fille(MenuButton menubutton_edit_deuxieme_tache_fille) {
        this.menubutton_edit_deuxieme_tache_fille = menubutton_edit_deuxieme_tache_fille;
    }

    public MenuButton getMenubutton_edit_lien_entre_taches() {
        return menubutton_edit_lien_entre_taches;
    }

    public void setMenubutton_edit_lien_entre_taches(MenuButton menubutton_edit_lien_entre_taches) {
        this.menubutton_edit_lien_entre_taches = menubutton_edit_lien_entre_taches;
    }

    public Text getTxt_edit_id_liens() {
        return txt_edit_id_liens;
    }

    public void setTxt_edit_id_liens(Text txt_edit_id_liens) {
        this.txt_edit_id_liens = txt_edit_id_liens;
    }

    public Button getButton_edit_ajouter_conditions() {
        return button_edit_ajouter_conditions;
    }

    public void setButton_edit_ajouter_conditions(Button button_edit_ajouter_conditions) {
        this.button_edit_ajouter_conditions = button_edit_ajouter_conditions;
    }

    public Button getButton_edit_supprimer_conditions() {
        return button_edit_supprimer_conditions;
    }

    public void setButton_edit_supprimer_conditions(Button button_edit_supprimer_conditions) {
        this.button_edit_supprimer_conditions = button_edit_supprimer_conditions;
    }

    public Button getButton_edit_ajouter_assertions() {
        return button_edit_ajouter_assertions;
    }

    public void setButton_edit_ajouter_assertions(Button button_edit_ajouter_assertions) {
        this.button_edit_ajouter_assertions = button_edit_ajouter_assertions;
    }

    public Button getButton_edit_modifier_assertions() {
        return button_edit_modifier_assertions;
    }

    public void setButton_edit_modifier_assertions(Button button_edit_modifier_assertions) {
        this.button_edit_modifier_assertions = button_edit_modifier_assertions;
    }

    public Button getButton_edit_supprimer_assertions() {
        return button_edit_supprimer_assertions;
    }

    public void setButton_edit_supprimer_assertions(Button button_edit_supprimer_assertions) {
        this.button_edit_supprimer_assertions = button_edit_supprimer_assertions;
    }

    public Button getButton_edit_enregistrer_conditions() {
        return button_edit_enregistrer_conditions;
    }

    public void setButton_edit_enregistrer_conditions(Button button_edit_enregistrer_conditions) {
        this.button_edit_enregistrer_conditions = button_edit_enregistrer_conditions;
    }

    public Text getTxt_edit_nom_tache_conditions() {
        return txt_edit_nom_tache_conditions;
    }

    public void setTxt_edit_nom_tache_conditions(Text txt_edit_nom_tache_conditions) {
        this.txt_edit_nom_tache_conditions = txt_edit_nom_tache_conditions;
    }

    public Text getTxt_edit_condition_selectionne() {
        return txt_edit_condition_selectionne;
    }

    public void setTxt_edit_condition_selectionne(Text txt_edit_condition_selectionne) {
        this.txt_edit_condition_selectionne = txt_edit_condition_selectionne;
    }

    public ListView getListview_edit_conditions() {
        return listview_edit_conditions;
    }

    public void setListview_edit_conditions(ListView listview_edit_conditions) {
        this.listview_edit_conditions = listview_edit_conditions;
    }

    public ListView getListview_edit_assertions() {
        return listview_edit_assertions;
    }

    public void setListview_edit_assertions(ListView listview_edit_assertions) {
        this.listview_edit_assertions = listview_edit_assertions;
    }

    public MenuButton getMenubutton_edit_type_condition() {
        return menubutton_edit_type_condition;
    }

    public void setMenubutton_edit_type_condition(MenuButton menubutton_edit_type_condition) {
        this.menubutton_edit_type_condition = menubutton_edit_type_condition;
    }

    public MenuButton getMenubutton_edit_operateur_logique_condition() {
        return menubutton_edit_operateur_logique_condition;
    }

    public void setMenubutton_edit_operateur_logique_condition(MenuButton menubutton_edit_operateur_logique_condition) {
        this.menubutton_edit_operateur_logique_condition = menubutton_edit_operateur_logique_condition;
    }

    public SplitPane getSplitPane_graph_edit() {
        return splitPane_graph_edit;
    }

    public void setSplitPane_graph_edit(SplitPane splitPane_graph_edit) {
        this.splitPane_graph_edit = splitPane_graph_edit;
    }

    public Button getButton_graph_ajouter_tag() {
        return button_graph_ajouter_tag;
    }

    public void setButton_graph_ajouter_tag(Button button_graph_ajouter_tag) {
        this.button_graph_ajouter_tag = button_graph_ajouter_tag;
    }

    public Button getButton_graph_supprimer_tag() {
        return button_graph_supprimer_tag;
    }

    public void setButton_graph_supprimer_tag(Button button_graph_supprimer_tag) {
        this.button_graph_supprimer_tag = button_graph_supprimer_tag;
    }

    public Button getButton_open() {
        return button_open;
    }

    public void setButton_open(Button button_open) {
        this.button_open = button_open;
    }

    public TextField getPopup_textfield_id() {
        return popup_textfield_id;
    }

    public void setPopup_textfield_id(TextField popup_textfield_id) {
        this.popup_textfield_id = popup_textfield_id;
    }

    public TextField getPopup_textfield_name() {
        return popup_textfield_name;
    }

    public void setPopup_textfield_name(TextField popup_textfield_name) {
        this.popup_textfield_name = popup_textfield_name;
    }

    /////// FONCTION UTILISEE DANS LES LISTENERS
    public void save(){
        applicationController.xmlFile.saveTextInFile();
    }

    public void refreshTreeFromXML() {
        System.out.println("Refresh button activated");
        save();
        System.out.println("saved!");
        applicationController.xmlParser.createTasksFromXML(applicationController.xmlFile.getXMLfilePath());
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        Stage newStage = new Stage();
        File file = fileChooser.showOpenDialog(newStage);
        System.out.println(file.toString());
        if (file != null) {
            applicationController.xmlFile.setXMLfilePath(file.getPath());
            System.out.println(applicationController.xmlFile.getXMLfilePath());
            applicationController.xmlFile.setTextFromFilePath();

            applicationController.xmlParser.createTasksFromXML(applicationController.xmlFile.getXMLfilePath());
            System.out.println(applicationController.xmlParser.getTasks().toString());
            applicationController.tasks = applicationController.xmlParser.getTasks();
            System.out.println(applicationController.tasks.toString());
            applicationController.createGraphFromTasks(applicationController.graph,applicationController.motherTasks,applicationController.tasks,applicationController.leafTasks);
        }
    }
}
