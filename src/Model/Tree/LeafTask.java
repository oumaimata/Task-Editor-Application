package Model.Tree;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by ladyn-totorosaure on 22/05/17.
 */
public class LeafTask extends Task{
    public List<Operation> operationList;

    public LeafTask(StringProperty idProperty, StringProperty nameProperty, List<Operation> operationList) {
        super(idProperty, nameProperty);
        this.operationList = operationList;
    }
}
