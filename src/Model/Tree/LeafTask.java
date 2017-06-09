package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by ladyn-totorosaure on 22/05/17.
 */
public class LeafTask extends Task{
    MotherTask mother;
    public ObservableList<Operation> operationList;

    public LeafTask(StringProperty idProperty, StringProperty nameProperty, ObservableList<Operation> operationList) {
        super(idProperty, nameProperty);
        this.operationList = operationList;
    }

    public LeafTask()
    {
        super();
        // operationList à init
    }

    public GlobalParameters.TypeConstructeur getConstructor() {
        return mother.getConstructor();
    }

    public Element toXml(Document doc)
    {
        Element leafTaskElement = super.toXml(doc);

        Element constructorElement = doc.createElement("constructor");
        constructorElement.setAttribute("type",getConstructor().getName());
        leafTaskElement.appendChild(constructorElement);

        return leafTaskElement;
    }
}
