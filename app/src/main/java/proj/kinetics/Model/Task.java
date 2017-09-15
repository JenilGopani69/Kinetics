package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 23/8/17.
 */

public class Task {
    private String due_date;

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

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
    private String d_taskid;

    public Task() {
    }

    public String getD_taskid() {
        return d_taskid;
    }

    public void setD_taskid(String d_taskid) {
        this.d_taskid = d_taskid;
    }

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


    public Task(String taskId, String projectId, String projectName, String taskName, String estimatedTime, String requiredTime, String status, String totalQty, String doneQty, String taskDetails, String priority) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskName = taskName;
        this.estimatedTime = estimatedTime;
        this.requiredTime = requiredTime;
        this.status = status;
        this.totalQty = totalQty;
        this.doneQty = doneQty;
        this.taskDetails = taskDetails;
        this.priority = priority;
    }
}