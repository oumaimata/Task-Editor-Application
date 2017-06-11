package Model.Tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by pierrelouislacorte on 10/06/2017.
 */
public class LeafTasks {
    private ObservableList<LeafTask> leaftasks;
    private Integer id;

    // constructeur sans argument
    public LeafTasks() {
        System.out.println("creation de la observablelist tasks avec task.extractor()");
        this.leaftasks = FXCollections.observableArrayList();
        id = 0;
    }

    public ObservableList<LeafTask> getTasks() {
        return leaftasks;
    }

    public void setTasks(ObservableList<LeafTask> leaftasks) {
        this.leaftasks = leaftasks;
    }

    // getter sur un id qui va s'incrementer a chaque call
    public Integer getId() {
        this.id = id + 1;
        return id;
    }

    // methode pour ajouter autant de tache que l'on veut
    public void addTask(LeafTask leafTask){
        System.out.println("Ajout d'une tâche à la liste des taches globale ");
        if (!leaftasks.contains(leafTask))
            this.leaftasks.add(leafTask);
    }

    public void removeTask(LeafTask leafTask){
        System.out.println("Tentative d'ajout d'une tâche à la liste des taches globale ");
        if (leaftasks.contains(leafTask)){
            System.out.println("Retrait d'une tâche à la liste des taches meres globale ");
            this.leaftasks.remove(leafTask);
        }
    }

    // methode pour clear la liste complete des taches
    public void clear(){
        getTasks().clear();
    }

    public String toString(){
        StringBuilder result = new StringBuilder("liste des taches: ");
        for(LeafTask task:leaftasks) {
            result.append(task.getNameProperty());
            result.append(" ");
            result.append(task.getIdProperty());
            result.append(", ");
        }
        return result.toString();
    }
}
