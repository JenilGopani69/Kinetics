package proj.kinetics.Model;

/**
 * Created by sai on 14/9/17.
 */

public class Mapping {
    String t_id, d_id;

    public Mapping(String t_id, String d_id) {
        this.t_id = t_id;
        this.d_id = d_id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }
}
