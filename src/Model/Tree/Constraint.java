package Model.Tree;

import javafx.beans.property.StringProperty;

/**
 * Created by ladyn-totorosaure on 06/06/17.
 */
public class Constraint {
    private StringProperty sujet;
    private StringProperty predicat;
    private StringProperty objet;

    public String getSujet() {
        return sujet.get();
    }

    public StringProperty sujetProperty() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet.set(sujet);
    }

    public String getPredicat() {
        return predicat.get();
    }

    public StringProperty predicatProperty() {
        return predicat;
    }

    public void setPredicat(String predicat) {
        this.predicat.set(predicat);
    }

    public String getObjet() {
        return objet.get();
    }

    public StringProperty objetProperty() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet.set(objet);
    }
}
