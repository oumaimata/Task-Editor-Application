package Model.Tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by pierrelouislacorte on 06/06/2017.
 */

/*
 Necessite de création d'un liste observable de toutes les taches du modèle
 */
public class Tasks {
    private ObservableList<Task> tasks;
    private Integer id;

    // constructeur sans argument
    public Tasks() {
        System.out.println("creation de la observablelist tasks avec task.extractor()");
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

    // methode pour ajouter autant de tache que l'on veut
    public void addTask(Task task){
        System.out.println("Ajout d'une tâche à la liste des taches globale ");
        this.tasks.add(task);
    }

    // methode d'ajout a la liste des taches une tache par défaut : nom + id
    // retourne la tache ainsi créée
    public Task addDefaultTache(){
        System.out.println("lancement de la methode addDefaultTache de Tasks");
        Task newTask = new Task();
        newTask.setIdProperty(getId().toString());
        addTask(newTask);
        System.out.println(tasks.toString());
        return newTask;
    }

    // methode pour clear la liste complete des taches
    public void clear(){
        getTasks().clear();
    }

    public String toString(){
        StringBuilder result = new StringBuilder("liste des taches: ");
        for(Task task:tasks) {
            result.append(task.getNameProperty());
            result.append(" ");
            result.append(task.getIdProperty());
            result.append(", ");
        }
        return result.toString();
    }


}
