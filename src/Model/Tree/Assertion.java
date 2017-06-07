package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ladyn-totorosaure on 07/06/17.
 */
public class Assertion {
    public ObjectProperty<GlobalParameters.OperateurLogique> operator;
    public StringProperty subject;
    public StringProperty predicate;
    public StringProperty object;
    public ObjectProperty<GlobalParameters.TypeAssertion> type;

    public Assertion() {
        operator = new SimpleObjectProperty<GlobalParameters.OperateurLogique>();
        subject = new SimpleStringProperty();
        predicate = new SimpleStringProperty();
        object = new SimpleStringProperty();
        type = new SimpleObjectProperty<GlobalParameters.TypeAssertion>();
    }

    public GlobalParameters.OperateurLogique getOperator() {
        return operator.get();
    }

    public ObjectProperty<GlobalParameters.OperateurLogique> operatorProperty() {
        return operator;
    }

    public void setOperator(GlobalParameters.OperateurLogique operator) {
        this.operator.set(operator);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getPredicate() {
        return predicate.get();
    }

    public StringProperty predicateProperty() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate.set(predicate);
    }

    public String getObject() {
        return object.get();
    }

    public StringProperty objectProperty() {
        return object;
    }

    public void setObject(String object) {
        this.object.set(object);
    }

    public GlobalParameters.TypeAssertion getType() {
        return type.get();
    }

    public ObjectProperty<GlobalParameters.TypeAssertion> typeProperty() {
        return type;
    }

    public void setType(GlobalParameters.TypeAssertion type) {
        this.type.set(type);
    }
}
