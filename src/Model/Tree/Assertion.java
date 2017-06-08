package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

/**
 * Created by ladyn-totorosaure on 07/06/17.
 */
public class Assertion {
    public StringProperty subject;
    public StringProperty predicate;
    public StringProperty object;
    public ObjectProperty<GlobalParameters.TypeAssertion> type;
    private BooleanProperty not;

    public Assertion() {
        subject = new SimpleStringProperty();
        predicate = new SimpleStringProperty();
        object = new SimpleStringProperty();
        type = new SimpleObjectProperty<GlobalParameters.TypeAssertion>();
        not = new SimpleBooleanProperty(false);
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
    public ObjectProperty<GlobalParameters.TypeAssertion> typeProperty() { return type; }
    public void setType(GlobalParameters.TypeAssertion type) { this.type.set(type); }
    public void setType(String name) { this.type.set(GlobalParameters.TypeAssertion.valueOf(name)); }

    public Boolean getNot() {
        return not.get();
    }
    public BooleanProperty notProperty() {
        return not;
    }
    public void setNot(Boolean _not) {
        this.not.set(_not);
    }

    public static Callback<Assertion, Observable[]> extractor() {
        return new Callback<Assertion, Observable[]>() {
            @Override
            public Observable[] call(Assertion param) {
                return new Observable[]{param.typeProperty(), param.subjectProperty(), param.predicateProperty(), param.objectProperty(), param.notProperty()};
            }
        };
    }
}
