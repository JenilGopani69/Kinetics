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
    // Get stored session data
    public HashMap<String, String> getTaskDetails(){
        HashMap<String, String> task = new HashMap<String, String>();

        task.put(KEY_AMOUNT, timerpref.getString(KEY_AMOUNT, "1"));
        task.put(KEY_DURATION, timerpref.getString(KEY_DURATION, "00:00:00"));
        task.put(KEY_LEFT, timerpref.getString(KEY_LEFT, "0"));


        return task;
    }


    public void clearTimer(){
        timereditor.clear();
        timereditor.commit();

    }


}
