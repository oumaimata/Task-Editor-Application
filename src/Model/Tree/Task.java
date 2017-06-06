package Model.Tree;

import Model.GlobalParameters;
import com.sun.javafx.collections.MappingChange;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

/**
 * Created by Théo on 17/05/2017.
 */
public class Task {
    IntegerProperty idProperty;
    StringProperty nameProperty;
    GlobalParameters.Nature nature;
    Map<Task, GlobalParameters.RelationAllen> linkToSister;
    List<Tag> tagList;
    List<Condition> conditionList;

    public Task(IntegerProperty idProperty, StringProperty nameProperty, GlobalParameters.Nature nature, Map<Task, GlobalParameters.RelationAllen> linkToSister, List<Tag> tagList, List<Condition> conditionList) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
        this.nature = nature;
        this.linkToSister = linkToSister;
        this.tagList = tagList;
        this.conditionList = conditionList;
    }

    public Task(IntegerProperty idProperty, StringProperty nameProperty) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
    }

    public Task() {
        System.out.println("creation d'une nouvelle tache avec les configurations par défaut : id = 0 et name = Task + id");
        this.idProperty = new SimpleIntegerProperty(0,"idproperty");
        this.nameProperty = new SimpleStringProperty("Task");
    }

    public void setIdProperty(int idProperty) {
        System.out.println("update de l'id d'une tâche, ancienne valeur " + this.getIdProperty() +", nouvelle valeur : " + idProperty);
        this.idProperty.set(idProperty);
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public int getIdProperty() {
        return idProperty.get();
    }

    public IntegerProperty idPropertyProperty() {
        return idProperty;
    }

    public static Callback<Task, Observable[]> extractor() {
        return new Callback<Task, Observable[]>() {
            @Override
            public Observable[] call(Task param) {
                return new Observable[]{param.idPropertyProperty(), param.namePropertyProperty()};
            }
        };
    }

    @Override
    public String toString() {
        return String.format("%s | %s", getIdProperty(), getNameProperty());
    }
}
