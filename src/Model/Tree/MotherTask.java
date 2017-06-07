package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Camille on 22/05/17.
 */
public class MotherTask extends Task{
    private GlobalParameters.TypeConstructeur constructor;

    public MotherTask(StringProperty idProperty, StringProperty nameProperty, GlobalParameters.TypeConstructeur constructor) {
        super(idProperty, nameProperty);
        this.constructor = constructor;
    }

    public MotherTask() {
        super();
    }

    public GlobalParameters.TypeConstructeur getConstructor() {
        return constructor;
    }

    public void setConstructor(GlobalParameters.TypeConstructeur constructor) {
        this.constructor = constructor;
    }
}
