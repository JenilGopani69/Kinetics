package proj.kinetics.Model;

/**
 * Created by sai on 9/9/17.
 */

public class DTask {
    String taskid,dtaskid,taskname;


    public DTask(String taskid, String dtaskid, String taskname) {
        this.taskid = taskid;
        this.dtaskid = dtaskid;
        this.taskname = taskname;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getDtaskid() {
        return dtaskid;
    }

    public void setDtaskid(String dtaskid) {
        this.dtaskid = dtaskid;
    }
}
