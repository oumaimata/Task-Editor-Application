package Model;


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
 */
public class XMLFile {
    private String XMLtext;

    public XMLFile() {

    }

    public void setTextFilePath() {
//        System.out.println(XML_file_path);
//        try (BufferedReader reader = Files.newBufferedReader(Paths.get(XML_file_path))) {
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//                XMLtext.add(line);
//            }
//        } catch (IOException x) {
//            System.err.format("IOException: %s%n", x);
//        }
        Path pathToFile = Paths.get("exempleXML.xml");
        Charset charset = Charset.defaultCharset();
        try {
           XMLtext = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getXMLtext() {
        return XMLtext;
    }

    public void setXMLtext(String XMLtext) {
        this.XMLtext = XMLtext;
    }
}
