package Model.Tree;

import Model.GlobalParameters;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Camille on 22/05/17.
 */
public class MotherTask extends Task{
    private GlobalParameters.TypeConstructeur constructor;
    ObservableList<LinkBetweenDaughter> linkBetweenDaughters;

    public MotherTask(StringProperty idProperty, StringProperty nameProperty, GlobalParameters.TypeConstructeur constructor) {
        super(idProperty, nameProperty);
        this.constructor = constructor;
        this.linkBetweenDaughters = FXCollections.observableArrayList(LinkBetweenDaughter.extractor());
    }

    public MotherTask() {
        super();
        this.linkBetweenDaughters = FXCollections.observableArrayList(LinkBetweenDaughter.extractor());
    }

    public GlobalParameters.TypeConstructeur getConstructor() {
        return constructor;
    }
    public void setConstructor(GlobalParameters.TypeConstructeur constructor) {
        this.constructor = constructor;
    }
    public void setConstructor(String name) {
        constructor = GlobalParameters.TypeConstructeur.valueOf(name);
    }

    public ObservableList<LinkBetweenDaughter> getLinkBetweenDaughters() {return linkBetweenDaughters;}
    public void setLinkBetweenDaughters(ObservableList<LinkBetweenDaughter> linkBetweenDaughters) {this.linkBetweenDaughters = linkBetweenDaughters;}
    public void addLinkBetweenDaugther(LinkBetweenDaughter link)
    {
        if (!linkBetweenDaughters.contains(link))
            linkBetweenDaughters.add(link);
    }
    public void removeLinkBetweenDaugther(LinkBetweenDaughter link)
    {
        if (linkBetweenDaughters.contains(link))
            linkBetweenDaughters.remove(link);
    }
}
