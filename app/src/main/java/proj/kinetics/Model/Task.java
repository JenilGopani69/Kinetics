package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 23/8/17.
 */

public class Task {
    @SerializedName("task_name")
    @Expose
    private String taskName;
    @SerializedName("task_details")
    @Expose
    private String taskDetails;
    @SerializedName("duedate")
    @Expose
    private String duedate;
    @SerializedName("estimated_time")
    @Expose
    private String estimatedTime;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("project_id")
    @Expose
    private String projectId;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
