package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sai on 24/8/17.
 */

public class TaskDetails {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("taskname")
    @Expose
    private String taskname;
    @SerializedName("taskdescription")
    @Expose
    private String taskdescription;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("estimated_time")
    @Expose
    private String estimatedTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("pdf_link")
    @Expose
    private String pdfLink;
    @SerializedName("video_link")
    @Expose
    private String videoLink;
    @SerializedName("qualitycheck")
    @Expose
    private List<Qualitycheck> qualitycheck = null;
    @SerializedName("dependenttask")
    @Expose
    private List<Dependenttask> dependenttask = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public List<Qualitycheck> getQualitycheck() {
        return qualitycheck;
    }

    public void setQualitycheck(List<Qualitycheck> qualitycheck) {
        this.qualitycheck = qualitycheck;
    }

    public List<Dependenttask> getDependenttask() {
        return dependenttask;
    }

    public void setDependenttask(List<Dependenttask> dependenttask) {
        this.dependenttask = dependenttask;
    }


}
