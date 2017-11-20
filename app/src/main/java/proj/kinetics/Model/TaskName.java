package proj.kinetics.Model;

/**
 * Created by sai on 14/9/17.
 */

public class TaskName {
    String name, id;

    public TaskName(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
