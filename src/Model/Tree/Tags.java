
package Model.Tree;

        import javafx.beans.property.SimpleStringProperty;
        import javafx.beans.property.StringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;

        import java.util.*;
/**
 * Created by oumaimatalouka on 6/8/17.
 */

public class Tags {

    public ObservableList<Tag> tags;

    public Tags(){
        tags = FXCollections.observableArrayList();
    }

    public ObservableList<Tag> getTags() {
        return tags;
    }

    public void setTags(ObservableList<Tag> tags) {
        this.tags = tags;
    }

    private boolean tagExists(String name) {
        Iterator<Tag> it = tags.iterator();
        boolean found = false;

        while (it.hasNext() && !found) {
            Tag t = it.next();
            if (t.getNameProperty().equals(name))
                found = true;
        }

        return found;
    }


    public void addTag(Tag tag) {
        if (!tagExists(tag.getNameProperty())) {
            tags.add(new Tag(tag.idPropertyProperty(), tag.namePropertyProperty()));
            return;
        }

        int i = 1;
        while (tagExists(tag.getNameProperty()+i))
            i++;

        StringProperty tagName = new SimpleStringProperty();
        tagName.setValue(tag.getNameProperty() + i);
        tags.add(new Tag(tagName));
    }

    public void addTag() {
        if (!tagExists(Tag.DEFAULT_TAG_NAME)) {
            tags.add(new Tag());
            return;
        }

        int i = 1;
        while (tagExists(Tag.DEFAULT_TAG_NAME + i))
            i++;
        StringProperty tagName = new SimpleStringProperty();
        tagName.setValue(Tag.DEFAULT_TAG_NAME + i);
        tags.add(new Tag(tagName));
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void setTagName(Tag tag, String name) {
        if (!tagExists(name))
            tag.setNameProperty(name);
    }

    public Tag addDefaultTag(){

        // verify if id is integer or string, we handle just strings here
        StringProperty tagName = new SimpleStringProperty();
        tagName.setValue("New Tag");
        Tag newTag = new Tag(tagName);


        /* verify if id is integer or string, we handle just strings here
        newTag.setIdProperty(0);
         */

        addTag(newTag);

        System.out.println(contentFromTags(tags));

        return newTag;
    }

    public void clear(){
        getTags().clear();
    }

    public String contentFromTags(ObservableList<Tag> tags){

        StringBuilder result = new StringBuilder("Liste des Tags ");

        for(Tag tag:tags) {
            result.append(tag.getIdProperty());
            result.append(" ");
            result.append(tag.getNameProperty());
            result.append(", ");
        }

        return result.toString();
    }
}
