package Model.Tree;

/**
 * Created by ladyn-totorosaure on 21/06/17.
 */
public class TreeAgent {
    private MotherTasks motherTasks = new MotherTasks(); // reference sur l'ensemble des taches meres du modèle
    private LeafTasks leafTasks = new LeafTasks(); // reference sur l'ensemble des taches feuilles du modèle

    public TreeAgent() {
        this.motherTasks = new MotherTasks();
        this.leafTasks = new LeafTasks();
    }

    public MotherTasks getMotherTasks() {
        return motherTasks;
    }

    public void setMotherTasks(MotherTasks motherTasks) {
        this.motherTasks = motherTasks;
    }

    public LeafTasks getLeafTasks() {
        return leafTasks;
    }

    public void setLeafTasks(LeafTasks leafTasks) {
        this.leafTasks = leafTasks;
    }

}
