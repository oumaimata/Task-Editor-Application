package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.StringProperty;

/**
 * Created by Théo on 17/05/2017.
 */
public class Condition {
    GlobalParameters.TypeCondition _type;

    public GlobalParameters.TypeCondition getType() { return _type; }
    public void setType(GlobalParameters.TypeCondition type) { this._type = type; }

    public class Assertion {
        StringProperty _subject;
        StringProperty _predicate;
        StringProperty _object;
        GlobalParameters.TypeAssertion _type;

        /*public String getSubject() { return _subject.getValue(); }
        public void setSubject(String subject) { this._subject = subject; }

        public String getPredicate() { return _predicate; }
        public void setPredicate(String predicate) { this._predicate = predicate; }

        public String getObject() { return _object; }
        public void setObject(String object) { this._object = object; }

        public GlobalParameters.TypeAssertion getType() { return _type; }
        public void setType(GlobalParameters.TypeAssertion type) { this._type = type; }*/
    }
}
