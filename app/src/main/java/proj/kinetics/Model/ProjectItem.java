package proj.kinetics.Model;

import java.io.Serializable;

/**
 * Created by sai on 19/7/17.
 */

public class ProjectItem implements Serializable {
    String name,description,nooftask,priority;

    public ProjectItem(String name, String description, String nooftask, String priority) {
        this.name = name;
        this.description = description;

        this.nooftask = nooftask;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getNooftask() {
        return nooftask;
    }

    public void setNooftask(String nooftask) {
        this.nooftask = nooftask;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
