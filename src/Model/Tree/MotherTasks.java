package Model.Tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by pierrelouislacorte on 10/06/2017.
 */
public class MotherTasks {
    private ObservableList<MotherTask> motherTasks; // Passer en ObservableMap<String,MotherTask>
    private Integer id;

    // constructeur sans argument
    public MotherTasks() {
        this.motherTasks = FXCollections.observableArrayList();
        id = 0;
    }

    public ObservableList<MotherTask> getTasks() {
        return motherTasks;
    }

    public void setTasks(ObservableList<MotherTask> motherTask) {
        this.motherTasks = motherTask;
    }

    // getter sur un id qui va s'incrementer a chaque call
    public Integer getId() {
        this.id = id + 1;
        return id;
    }

    public void createLinkBetweenTwoTasks(MotherTask Mother, Task Daugther){
        System.out.println("Creation d'un lien de parenté entre "+Mother.getIdProperty() + " et "+ Daugther.getIdProperty());
        Mother.addSubTask(Daugther.getIdProperty());
    }

    public void createLinkBetweenTwoTasks(MotherTask Mother, MotherTask Daugther){
        System.out.println("Creation d'un lien de parenté entre "+Mother.getIdProperty() + " et "+ Daugther.getIdProperty());
        Mother.addSubTask(Daugther.getIdProperty());
    }

    // methode pour ajouter autant de tache que l'on veut
    public void addTask(MotherTask task){
        System.out.println("Tentative d'ajout d'une tâche à la liste des taches globale ");
        if (!motherTasks.contains(task)){
            System.out.println("Ajout d'une tâche à la liste des taches meres globale ");
            this.motherTasks.add(task);
        }

    }

    // méthode pour supprimer une task de la liste des taches meres
    public void removeTask(MotherTask task){
        System.out.println("Tentative de retrait d'une tâche à la liste des taches globale ");
        if (motherTasks.contains(task)){
            System.out.println("Retrait d'une tâche à la liste des taches meres globale ");
            this.motherTasks.remove(task);
        }
    }

    // methode d'ajout a la liste des taches une tache par défaut : nom + id
    // retourne la tache ainsi créée
    public MotherTask addDefaultTache(){
        System.out.println("lancement de la methode addDefaultTache de Tasks");
        MotherTask newTask = new MotherTask();
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
        StringBuilder result = new StringBuilder("liste des tache meres: ");
        for(MotherTask task:motherTasks) {
            result.append(task.getNameProperty());
            result.append(" ");
            result.append(task.getIdProperty());
            result.append(", ");
        }
        return result.toString();
    }
}
