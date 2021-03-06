package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Théo on 17/05/2017.
 */
public class Task {
    StringProperty idProperty;
    StringProperty nameProperty;
    GlobalParameters.Nature nature;
    List<Tag> tagList;
    ObservableList<Condition> conditionList;
    List<Object> context;

    public Task(StringProperty idProperty, StringProperty nameProperty) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
    }

    public Task(StringProperty idProperty, StringProperty nameProperty, ObservableList<Condition> observableList) {
        this.idProperty = idProperty;
        this.nameProperty = nameProperty;
        this.conditionList = observableList;
    }

    public Task() {
        //System.out.println("creation d'une nouvelle tache avec les configurations par défaut : id = 0 et name = Task + id");
        this.idProperty = new SimpleStringProperty(0,"idproperty");
        this.nameProperty = new SimpleStringProperty("Task");
        this.conditionList = FXCollections.observableArrayList(Condition.extractor());
    }

    public void setIdProperty(String idProperty) {
        //System.out.println("update de l'id d'une tâche, ancienne valeur " + this.getIdProperty() +", nouvelle valeur : " + idProperty);
        this.idProperty.setValue(idProperty);
    }

    public void setNameProperty(String nameProperty) {
        if (!nameProperty.isEmpty())
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

    public ObservableList<Condition> getConditionList() { return conditionList; }

    public void setConditionList(ObservableList<Condition> conditionList) { this.conditionList = conditionList; }

    public void addCondition(Condition condition)
    {
        if (!conditionList.contains(condition))
            conditionList.add(condition);
    }
    public void removeCondition(Condition condition)
    {
        if (conditionList.contains(condition))
            conditionList.remove(condition);
    }

    public static Callback<Task, Observable[]> extractor() {
        return new Callback<Task, Observable[]>() {
            @Override
            public Observable[] call(Task param) {
                return new Observable[]{param.idPropertyProperty(), param.namePropertyProperty()};
            }
        };
    }

    public Element toXml(Document doc)
    {
        Element taskElement = doc.createElement("task");
        taskElement.setAttribute("id",getIdProperty());
        taskElement.setAttribute(getNature().getName(),"true");
        taskElement.setAttribute("name",getNameProperty());

        // TODO Context

        Element conditionsElement = doc.createElement("conditions");
        int nb = conditionList.size();
        for (int condition_count=0; condition_count<nb; ++nb)
        {
            Element condtionElement = conditionList.get(condition_count).toXml(doc);
            conditionsElement.appendChild(condtionElement);
        }
        taskElement.appendChild(conditionsElement);
        return taskElement;
    }

    @Override
    public String toString() {
        return ""; //String.format("%s | %s | %s", getIdProperty(), getNameProperty(), getNature().getName());
    }

    public void print()
    {
        System.out.println("\n"+toString());
        System.out.println("Conditions : ");
        for (int i=0; i<conditionList.size();++i)
        {
            conditionList.get(i).print();
        }

    }

}
