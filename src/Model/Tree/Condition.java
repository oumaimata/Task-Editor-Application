package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by Théo on 17/05/2017.
 */
public class Condition {
    public ObjectProperty<GlobalParameters.OperateurLogique> operator;
    private ObjectProperty<GlobalParameters.TypeCondition> type;
    private StringProperty id;
    private List<Assertion> assertionList;

    public Condition() {
        operator = new SimpleObjectProperty<GlobalParameters.OperateurLogique>();
        type = new SimpleObjectProperty<GlobalParameters.TypeCondition>();
        id = new SimpleStringProperty();
        //assertionList
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

    public List<Assertion> getAssertionList() {
        return assertionList;
    }
    public void setAssertionList(List<Assertion> assertionList) {
        this.assertionList = assertionList;
    }
}
