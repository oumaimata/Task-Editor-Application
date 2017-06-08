package Model.Tree;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by ladyn-totorosaure on 22/05/17.
 */
public class LeafTask extends Task{
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
}
