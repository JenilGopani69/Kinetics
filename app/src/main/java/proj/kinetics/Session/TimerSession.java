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
    public static final String KEY_REQ= "req";


    //is similar task added
    private static final String IS_STASK= "Is_Stask";




    private static final String PREF_NAME = "TimerPref";

    public TimerSession(Context context) {
        this._context = context;
        timerpref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        timereditor = timerpref.edit();
    }


    public void createTimerData(String unit, String time,String left,String req){


        timereditor.putString(KEY_AMOUNT, unit);
        timereditor.putString(KEY_DURATION, time);
        timereditor.putString(KEY_LEFT, left);
        timereditor.putString(KEY_REQ, req);
timereditor.putBoolean(IS_STASK,true);

        // commit changes
        timereditor.commit();
    }





    // Get stored session data
    public HashMap<String, String> getTaskDetails(){
        HashMap<String, String> task = new HashMap<String, String>();

        task.put(KEY_AMOUNT, timerpref.getString(KEY_AMOUNT, "0"));
        task.put(KEY_DURATION, timerpref.getString(KEY_DURATION, "00:00:00"));
        task.put(KEY_LEFT, timerpref.getString(KEY_LEFT, "0"));
        task.put(KEY_REQ, timerpref.getString(KEY_REQ, "0"));



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
