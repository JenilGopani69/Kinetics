package proj.kinetics.Session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by sai on 31/8/17.
 */

public class TimerSessionForAddTask {
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_LEFT = "left";
    public static final String KEY_REQ = "req";
    //is similar task added
    private static final String IS_STASK = "Is_Stask";
    private static final String PREF_NAME = "s_task_pref";
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor prefeditior;
    Context _context;
    int PRIVATE_MODE = 0;

    public TimerSessionForAddTask(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefeditior = pref.edit();
    }


    public void createTimerData(String unit, String time, String left, String id, String req, String taskid) {


        prefeditior.putString(KEY_AMOUNT, unit);
        prefeditior.putString(KEY_DURATION, time);
        prefeditior.putString(KEY_LEFT, left);

        prefeditior.putString(KEY_REQ, req);
        prefeditior.putString(taskid, id);

        prefeditior.putBoolean(IS_STASK, true);
        // commit changes
        prefeditior.commit();
    }


    public void addId(String id, String taskid) {


        prefeditior.putString(taskid, id);

        prefeditior.putBoolean(IS_STASK, true);
        // commit changes
        prefeditior.commit();
    }


    // Get stored session data
    public HashMap<String, String> getTaskDetails(String taskid) {
        HashMap<String, String> task = new HashMap<String, String>();

        task.put(KEY_AMOUNT, pref.getString(KEY_AMOUNT, "0"));
        task.put(KEY_DURATION, pref.getString(KEY_DURATION, "00:00:00"));
        task.put(KEY_LEFT, pref.getString(KEY_LEFT, "0"));
        task.put(KEY_REQ, pref.getString(KEY_REQ, "0"));
        task.put(taskid, pref.getString(taskid, "0"));


        return task;
    }


    public void clearTimer() {
        prefeditior.clear();
        prefeditior.commit();

    }


}
