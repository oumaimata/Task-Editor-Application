package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Théo on 17/05/2017.
 */
public class Condition {
    public ObjectProperty<GlobalParameters.OperateurLogique> operator;
    private ObjectProperty<GlobalParameters.TypeCondition> type;
    private StringProperty id;
    private ObservableList<Assertion> assertionList;

    public Condition() {
        operator = new SimpleObjectProperty<GlobalParameters.OperateurLogique>();
        type = new SimpleObjectProperty<GlobalParameters.TypeCondition>();
        id = new SimpleStringProperty();
        this.assertionList = FXCollections.observableArrayList(Assertion.extractor());
    }

    public GlobalParameters.OperateurLogique getOperator() {
        return operator.get();
    }
    public ObjectProperty<GlobalParameters.OperateurLogique> operatorProperty() {
        return operator;
    }
    public void setOperator(GlobalParameters.OperateurLogique operator) { this.operator.set(operator); }
    public void setOperator(String name) { operator.set(GlobalParameters.OperateurLogique.valueOf(name)); }

    public String getId() {
        return id.get();
    }
    public StringProperty idProperty() {
        return id;
    }
    public void setId(String id) {
        this.id.set(id);
    }

    public GlobalParameters.TypeCondition getType() {
        return type.get();
    }
    public ObjectProperty<GlobalParameters.TypeCondition> typeProperty() {
        return type;
    }
    public void setType(GlobalParameters.TypeCondition type) { this.type.set(type); }
    public void setType(String name) { this.type.set(GlobalParameters.TypeCondition.valueOf(name)); }

    public ObservableList<Assertion> getAssertionList() {
        return assertionList;
    }
    public void setAssertionList(ObservableList<Assertion> assertionList) {
        this.assertionList = assertionList;
    }
    public void addAssertion(Assertion assertion)
    {
        if (!assertionList.contains(assertion))
            assertionList.add(assertion);
    }
    public void removeAssertion(Assertion assertion)
    {
        if (assertionList.contains(assertion))
            assertionList.remove(assertion);
    }

    public Element toXml(Document doc)
    {
        Element conditionElement = doc.createElement(getType().getName());
        conditionElement.setAttribute("id",getId() );

        Element assertions = doc.createElement(getOperator().getName());
        int nb = assertionList.size();
        for (int assertion_count=0; assertion_count<nb; ++assertion_count)
        {
            Element assertion = assertionList.get(assertion_count).toXml(doc);
            assertions.appendChild(assertion);
        }
        conditionElement.appendChild(assertions);
        return conditionElement;
    }

    public static Callback<Condition, Observable[]> extractor() {
        return new Callback<Condition, Observable[]>() {
            @Override
            public Observable[] call(Condition param) {
                return new Observable[]{param.idProperty(), param.typeProperty(), param.operatorProperty()};
            }
        };
    }
}
