package Model.Tree;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Théo on 17/05/2017.
 */
public class Tag {

    static String DEFAULT_TAG_NAME = "Nouveau tag";

    //verify of tag.id is the same as tag.name, we suppose that it is true here and handle just the id for a matter of quick implementation
    private StringProperty idProperty;
    private StringProperty nameProperty;


    public Tag() {
        this.idProperty = new SimpleStringProperty(DEFAULT_TAG_NAME);
        this.nameProperty = new SimpleStringProperty(DEFAULT_TAG_NAME);
    }

    public Tag(StringProperty nameProperty){
        this.idProperty = nameProperty;
        this.nameProperty = nameProperty;
    }
    public Tag(StringProperty idProperty, StringProperty nameProperty) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
    }


    //Getters and Setters
    public String getIdProperty() {
        return idProperty.get();
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public void setIdProperty(String id) {
        this.idProperty.set(id);
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public StringProperty idPropertyProperty() {
        return idProperty;
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }
}
