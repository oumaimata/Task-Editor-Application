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
    private ObjectProperty<GlobalParameters.TypeCondition> type;

    private StringProperty id;

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

    public void setType(GlobalParameters.TypeCondition type) {
        this.type.set(type);
    }

    private List<Assertion> assertionList;

    public List<Assertion> getAssertionList() {
        return assertionList;
    }

    public void setAssertionList(List<Assertion> assertionList) {
        this.assertionList = assertionList;
    }
}
