package proj.kinetics.Session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by sai on 31/8/17.
 */

public class TimerSession {
    SharedPreferences timerpref;

    // Editor for Shared preferences
    SharedPreferences.Editor timereditor;
    Context _context;
    int PRIVATE_MODE = 0;




    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DURATION= "duration";
    public static final String KEY_LEFT= "left";


    //is similar task added
    private static final String IS_STASK= "Is_Stask";


    ///
    public static final String KEY_S_TASKID= "staskid";
    public static final String KEY_S_TASKAMT= "staskamount";
    public static final String KEY_S_TASKDUR= "staskduration";
    public static final String KEY_S_TASKLEFT= "staskleft";
    public static final String KEY_S_TASKREQ= "staskreq";



    private static final String PREF_NAME = "TimerPref";

    public TimerSession(Context context) {
        this._context = context;
        timerpref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        timereditor = timerpref.edit();
    }


    public void createTimerData(String unit, String time,String left){


        timereditor.putString(KEY_AMOUNT, unit);
        timereditor.putString(KEY_DURATION, time);
        timereditor.putString(KEY_LEFT, left);


        // commit changes
        timereditor.commit();
    }


    public void addStask(String staskid,String staskreq){
        timereditor.putString(KEY_S_TASKID, staskid);
        timereditor.putString(KEY_S_TASKREQ, staskreq);

        timereditor.putBoolean(IS_STASK, true);

    }


    public void addStaskDetails(String staskamount,String staskduration,String staskleft ){
        timereditor.putString(KEY_S_TASKAMT, staskamount);
        timereditor.putString(KEY_S_TASKDUR, staskduration);
        timereditor.putString(KEY_S_TASKLEFT, staskleft);
        timereditor.commit();

    }


    // Get stored session data
    public HashMap<String, String> getTaskDetails(){
        HashMap<String, String> task = new HashMap<String, String>();

        task.put(KEY_AMOUNT, timerpref.getString(KEY_AMOUNT, "0"));
        task.put(KEY_DURATION, timerpref.getString(KEY_DURATION, "00:00:00"));
        task.put(KEY_LEFT, timerpref.getString(KEY_LEFT, "0"));


        return task;
    }



    //
    public HashMap<String, String> getStaskDetails(){
        HashMap<String, String> task = new HashMap<String, String>();

        task.put(KEY_S_TASKAMT, timerpref.getString(KEY_S_TASKAMT, "0"));
        task.put(KEY_S_TASKID, timerpref.getString(KEY_S_TASKID, ""));
        task.put(KEY_S_TASKDUR, timerpref.getString(KEY_S_TASKDUR, "00:00:00"));
        task.put(KEY_S_TASKLEFT, timerpref.getString(KEY_S_TASKLEFT, "0"));
        task.put(KEY_S_TASKREQ, timerpref.getString(KEY_S_TASKREQ, "0"));



        return task;
    }


    public void clearTimer(){
        timereditor.clear();
        timereditor.commit();

    }

    public boolean isLoggedIn(){
        return timerpref.getBoolean(IS_STASK, false);
    }
}
