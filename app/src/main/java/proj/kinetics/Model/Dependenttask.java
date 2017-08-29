package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sai on 24/8/17.
 */

public class Dependenttask {
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
    private List<Qualitycheck_> qualitycheck = null;

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

    public List<Qualitycheck_> getQualitycheck() {
        return qualitycheck;
    }

    public void setQualitycheck(List<Qualitycheck_> qualitycheck) {
        this.qualitycheck = qualitycheck;
    }

}