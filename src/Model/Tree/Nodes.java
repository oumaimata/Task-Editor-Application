package Model.Tree;

import com.yworks.yfiles.graph.INode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierrelouislacorte on 07/06/2017.
 */
public class Nodes {
    private List<INode> nodes;

    public Nodes() {
        System.out.println("creation de la liste vide des noeuds du projet");
        this.nodes = new ArrayList<>();
    }

    public void addNode(INode node){
        if(this.nodes.contains(node)){
            System.out.println("le noeud existe déjà a la liste des noeuds");
        }else{
            System.out.println("Ajout d'un noeud à la liste des noeuds globaux ");
            this.nodes.add(node);
        }
        System.out.println(toString());
        System.out.println("La liste des noeuds est de longueur: " + nodes.size());
    }

    public List<INode> getNodes() {
        return nodes;
    }

    public void setNodes(List<INode> nodes) {
        this.nodes = nodes;
    }

    public String toString(){
        StringBuilder result = new StringBuilder("liste des noeuds, contient les tags: ");
        for(INode node:nodes) {
            Task task = (Task) node.getTag();
            result.append("Name- ");
            result.append(task.getNameProperty());
            result.append(" id- ");
            result.append(task.getIdProperty());
            result.append("; ");
        }
        return result.toString();
    }
}
