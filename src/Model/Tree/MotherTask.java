package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Camille on 22/05/17.
 */
public class MotherTask extends Task{
    private GlobalParameters.TypeConstructeur constructor;

    public MotherTask(IntegerProperty idProperty, StringProperty nameProperty, GlobalParameters.TypeConstructeur constructor) {
        super(idProperty, nameProperty);
        this.constructor = constructor;
    }
}
