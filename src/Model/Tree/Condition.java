package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by Théo on 17/05/2017.
 */
public class Condition {
    private GlobalParameters.TypeCondition type;

    public GlobalParameters.TypeCondition getType() { return type; }
    public void setType(GlobalParameters.TypeCondition type) { this.type = type; }

    public class Assertion {
        public StringProperty subject;
        public StringProperty predicate;
        public StringProperty object;
        public GlobalParameters.TypeAssertion type;


    }

    private List<Assertion> assertionList;
}
