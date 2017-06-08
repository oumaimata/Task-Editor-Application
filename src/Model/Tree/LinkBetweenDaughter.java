package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

/**
 * Created by ladyn-totorosaure on 07/06/17.
 * This class represent the relation between two daughter tasks.
 */
public class LinkBetweenDaughter {
    private StringProperty leftDaughter;
    private ObjectProperty<GlobalParameters.RelationAllen> relation;
    private StringProperty rightDaughter;

    public LinkBetweenDaughter() {
        this.leftDaughter = new SimpleStringProperty();
        this.relation = new SimpleObjectProperty<GlobalParameters.RelationAllen>();
        this.rightDaughter = new SimpleStringProperty();
    }

    public LinkBetweenDaughter(StringProperty leftDaughter, ObjectProperty<GlobalParameters.RelationAllen> relation, StringProperty rightDaughter) {
        this.leftDaughter = leftDaughter;
        this.relation = relation;
        this.rightDaughter = rightDaughter;
    }

    public String getLeftDaughter() {
        return leftDaughter.get();
    }
    public StringProperty leftDaughterProperty() {
        return leftDaughter;
    }
    public void setLeftDaughter(String leftDaughter) {
        this.leftDaughter.set(leftDaughter);
    }

    public GlobalParameters.RelationAllen getRelation() {
        return relation.get();
    }
    public ObjectProperty<GlobalParameters.RelationAllen> relationProperty() {
        return relation;
    }
    public void setRelation(GlobalParameters.RelationAllen relation) {
        this.relation.set(relation);
    }

    public String getRightDaughter() {
        return rightDaughter.get();
    }
    public StringProperty rightDaughterProperty() {
        return rightDaughter;
    }
    public void setRightDaughter(String rightDaughter) {
        this.rightDaughter.set(rightDaughter);
    }

    public static Callback<LinkBetweenDaughter, Observable[]> extractor() {
        return new Callback<LinkBetweenDaughter, Observable[]>() {
            @Override
            public Observable[] call(LinkBetweenDaughter param) {
                return new Observable[]{param.leftDaughterProperty(), param.relationProperty(), param.rightDaughterProperty()};
            }
        };
    }
}
