package Model.Tree;

import Model.GlobalParameters;
import com.sun.javafx.collections.MappingChange;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Théo on 17/05/2017.
 */
public class Task {
    IntegerProperty idProperty;
    StringProperty nameProperty;
    GlobalParameters.Nature nature;
    Map<Task, GlobalParameters.RelationAllen> linkToSister;
    List<Tag> tagList;
    List<Condition> conditionList;

}
