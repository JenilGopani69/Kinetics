package proj.kinetics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 25/8/17.
 */

public class Qualitycheck {
    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }


    String userid,taskid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Qualitycheck(String status, String id, String descripton, String imageLink, String videoLink) {
        this.status = status;
        this.id = id;
        this.descripton = descripton;
        this.imageLink = imageLink;
        this.videoLink = videoLink;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("descripton")
    @Expose
    private String descripton;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("video_link")
    @Expose
    private String videoLink;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

}