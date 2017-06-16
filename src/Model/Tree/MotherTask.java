package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Camille on 22/05/17.
 */
public class MotherTask extends Task{

    private GlobalParameters.TypeConstructeur constructor;
    ObservableList<String> subTaskList;
    ObservableList<LinkBetweenDaughter> linkBetweenDaughters;

    public MotherTask(StringProperty idProperty, StringProperty nameProperty, GlobalParameters.TypeConstructeur constructor) {
        super(idProperty, nameProperty);
        this.constructor = constructor;
        this.linkBetweenDaughters = FXCollections.observableArrayList(LinkBetweenDaughter.extractor());
        this.subTaskList = FXCollections.observableArrayList();
    }

    public MotherTask(Task task) {
        super(task.idPropertyProperty(),task.namePropertyProperty(),task.getConditionList());
        this.linkBetweenDaughters = FXCollections.observableArrayList(LinkBetweenDaughter.extractor());
        this.subTaskList = FXCollections.observableArrayList();
    }

    public MotherTask(MotherTask task) {
        super(task.idPropertyProperty(),task.namePropertyProperty(),task.getConditionList());
        this.linkBetweenDaughters = task.getLinkBetweenDaughters();
        this.subTaskList = task.getSubTaskList();
    }

    public MotherTask() {
        super();
        this.linkBetweenDaughters = FXCollections.observableArrayList(LinkBetweenDaughter.extractor());
        this.subTaskList = FXCollections.observableArrayList();
    }

    public GlobalParameters.TypeConstructeur getConstructor() {
        return constructor;
    }
    public void setConstructor(GlobalParameters.TypeConstructeur constructor) {
        this.constructor = constructor;
    }
    public void setConstructor(String name) {
        constructor = GlobalParameters.TypeConstructeur.valueOf(name);
    }

    public ObservableList<String> getSubTaskList() {return subTaskList;}
    public void setSubTaskList(ObservableList<String> subTaskList) {this.subTaskList = subTaskList;}

    public void addSubTask(String subTask)
    {
        if (!subTaskList.contains(subTask))
            subTaskList.add(subTask);
    }
    public void removeSubTask(String subTask)
    {
        if (subTaskList.contains(subTask))
            subTaskList.remove(subTask);
    }

    public ObservableList<LinkBetweenDaughter> getLinkBetweenDaughters() {return linkBetweenDaughters;}
    public void setLinkBetweenDaughters(ObservableList<LinkBetweenDaughter> linkBetweenDaughters) {this.linkBetweenDaughters = linkBetweenDaughters;}
    public void addLinkBetweenDaugther(LinkBetweenDaughter link)
    {
        if (!linkBetweenDaughters.contains(link))
            linkBetweenDaughters.add(link);
    }
    public void removeLinkBetweenDaugther(LinkBetweenDaughter link)
    {
        if (linkBetweenDaughters.contains(link))
            linkBetweenDaughters.remove(link);
    }

    public Element toXml(Document doc)
    {
        Element motherTaskElement = super.toXml(doc);

        Element constructorElement = doc.createElement("constructor");
        constructorElement.setAttribute("type",getConstructor().getName());
        int nb = linkBetweenDaughters.size();
        for (int link_count=0; link_count<nb; ++link_count)
        {
            Element linkElement = linkBetweenDaughters.get(link_count).toXml(doc);
            constructorElement.appendChild(linkElement);
        }
        motherTaskElement.appendChild(constructorElement);

        Element subtasksElement = doc.createElement("subtasks");
        nb = subTaskList.size();
        for (int subtask_count=0; subtask_count<nb; ++subtask_count)
        {
            Element subtaskElement = doc.createElement("subtask");
            subtaskElement.setAttribute("id",subTaskList.get(subtask_count));
            subtasksElement.appendChild(subtaskElement);
        }
        motherTaskElement.appendChild(subtasksElement);

        return motherTaskElement;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void print()
    {
        super.print();
        System.out.println("Relations : ");
        for (int i=0; i<linkBetweenDaughters.size(); ++i)
        {
            linkBetweenDaughters.get(i).print();
        }
        System.out.println("Filles : ");
        for (int i=0; i<subTaskList.size(); ++i)
        {
            System.out.println(subTaskList.get(i));
        }
    }

}
