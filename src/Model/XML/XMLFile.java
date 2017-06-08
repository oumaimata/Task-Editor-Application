package Model.XML;


import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private String XMLtext;

    public XMLFile() {

    }

    public void setTextFilePath() {
        // creating the object path to the file
        Path pathToFile = Paths.get("exempleXML.xml");
        Charset charset = Charset.defaultCharset();
        try {
            // getting the text of the file in the string
           XMLtext = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method that return the text of the file
    public String getXMLtext() {
        return XMLtext;
    }

    public void setXMLtext(String XMLtext) {
        this.XMLtext = XMLtext;
    }
}
