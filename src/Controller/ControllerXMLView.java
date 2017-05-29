package Controller;


import Model.XMLFile;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import start.XMLEditor;

/**
 * Created by ladyn-totorosaure on 25/05/17.
 */
public class ControllerXMLView {
    private XMLFile model;

    @FXML
    BorderPane borderPane;
    CodeArea codeArea;

    public ControllerXMLView(){
        model = new XMLFile();
    }

    public void initialize(){
        codeArea = new CodeArea();
        borderPane.setCenter(codeArea);

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        model.setTextFilePath();
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, XMLEditor.computeHighlighting(newText));
        });
        codeArea.replaceText(0,0,model.getXMLtext());

    }

}
