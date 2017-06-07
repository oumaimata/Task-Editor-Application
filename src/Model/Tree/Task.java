package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by Théo on 17/05/2017.
 */
public class Task {
    StringProperty idProperty;
    StringProperty nameProperty;
    GlobalParameters.Nature nature;
    List<String> subTaskList;
    List<LinkBetweenDaughter> linkBetweenDaughters;
    List<Tag> tagList;
    List<Condition> conditionList;
    List<Object> context;




    public Task(StringProperty idProperty, StringProperty nameProperty) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
    }

    public Task() {
        System.out.println("creation d'une nouvelle tache avec les configurations par défaut : id = 0 et name = Task + id");
        this.idProperty = new SimpleStringProperty(0,"idproperty");
        this.nameProperty = new SimpleStringProperty("Task");
    }

    public void setIdProperty(String idProperty) {
        System.out.println("update de l'id d'une tâche, ancienne valeur " + this.getIdProperty() +", nouvelle valeur : " + idProperty);
        this.idProperty.setValue(idProperty);
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

    public String getIdProperty() {
        return idProperty.get();
    }

    public StringProperty idPropertyProperty() {
        return idProperty;
    }

    public GlobalParameters.Nature getNature() { return nature; }

    public void setNature(GlobalParameters.Nature nature) { this.nature = nature; }

    public List<String> getSubTaskList() {return subTaskList;}

    public void setSubTaskList(List<String> subTaskList) {this.subTaskList = subTaskList;}

    public List<LinkBetweenDaughter> getLinkBetweenDaughters() {return linkBetweenDaughters;}

    public void setLinkBetweenDaughters(List<LinkBetweenDaughter> linkBetweenDaughters) {this.linkBetweenDaughters = linkBetweenDaughters;}

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
