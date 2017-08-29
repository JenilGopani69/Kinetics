package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 23/8/17.
 */

public class Task {
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("task_name")
    @Expose
    private String taskName;
    @SerializedName("estimated_time")
    @Expose
    private String estimatedTime;
    @SerializedName("required_time")
    @Expose
    private String requiredTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("done_qty")
    @Expose
    private String doneQty;
    @SerializedName("task_details")
    @Expose
    private String taskDetails;
    @SerializedName("priority")
    @Expose
    private String priority;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(String requiredTime) {
        this.requiredTime = requiredTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getDoneQty() {
        return doneQty;
    }

    public void setDoneQty(String doneQty) {
        this.doneQty = doneQty;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}