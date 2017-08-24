package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sai on 23/8/17.
 */

public class Example {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("task")
    @Expose
    private List<Task> task = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }


}