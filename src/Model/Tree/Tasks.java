package Model.Tree;

import com.yworks.yfiles.graph.INode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by pierrelouislacorte on 06/06/2017.
 */

/*
 Necessite de création d'un liste observable de toutes les taches du modèle
 */
public class Tasks {
    private ObservableList<Task> tasks; // Passer en ObservableMap<String,Task>
    private Integer id;

    // constructeur sans argument
    public Tasks() {
        this.tasks = FXCollections.observableArrayList(Task.extractor());
        id = 0;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    // getter sur un id qui va s'incrementer a chaque call
    public Integer getId() {
        this.id = id + 1;
        return id;
    }

    public void createLinkBetweenTwoTasks(MotherTask Mother, Task Daugther){
        Mother.addSubTask(Daugther.getIdProperty());
    }

    // methode pour ajouter autant de tache que l'on veut
    public void addTask(Task task){
        if (!tasks.contains(task))
            this.tasks.add(task);
    }

    public void removeTask(Task task){
        if (tasks.contains(task)){
            this.tasks.remove(task);
        }
    }

    // methode d'ajout a la liste des taches une tache par défaut : nom + id
    // retourne la tache ainsi créée
    public Task addDefaultTache(){
        Task newTask = new Task();
        newTask.setIdProperty(getId().toString());
        addTask(newTask);
        System.out.println(toString());
        return newTask;
    }

    // methode pour clear la liste complete des taches
    public void clear(){
        getTasks().clear();
    }

    public String toString(){
        StringBuilder result = new StringBuilder("liste des tâches: ");
        for(Task task:tasks) {
            result.append(task.getNameProperty());
            result.append(" ");
            result.append(task.getIdProperty());
            result.append(", ");
        }
        return result.toString();
    }


}
