package Model.XML;


import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladyn-totorosaure on 27/05/17.
 * This class is used to contain the XML file and its content.
 */
public class XMLFile {
    // attribute containing the text of the XML File
    private StringProperty XMLtext;
    private StringProperty XMLfilePath;

    public XMLFile(String XMLfilePath) {
        this.XMLfilePath = new SimpleStringProperty(XMLfilePath);
        this.XMLtext = new SimpleStringProperty();
    }

    public XMLFile() {
        this.XMLfilePath = new SimpleStringProperty();
        this.XMLtext = new SimpleStringProperty();
        XMLtext.setValue("");
        XMLfilePath.setValue("test.xml");
    }

    public StringProperty XMLtextProperty() {
        return XMLtext;
    }

    public String getXMLfilePath() {
        return XMLfilePath.get();
    }

    public StringProperty XMLfilePathProperty() {
        return XMLfilePath;
    }

    public void setXMLfilePath(String XMLfilePath) {
        this.XMLfilePath.set(XMLfilePath);
    }

    public String getXMLtext() {
        return XMLtext.get();
    }

    public void setXMLtext(String XMLtext) {
        this.XMLtext.set(XMLtext);
    }

    public void setTextFromFilePath() {
        // creating the object path to the file
        Path pathToFile = Paths.get(getXMLfilePath());
        try {
            // getting the text of the file in the string
           XMLtext.setValue(new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTextInFile(){
        File newFile = new File(getXMLfilePath());
        try{
            if(newFile.exists()){newFile.delete();}
            newFile.createNewFile();
            FileWriter writer = new FileWriter(newFile);
            writer.write(getXMLtext());
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println("Erreur : "+e.toString());
        }
    }

}
