package proj.kinetics.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import proj.kinetics.Model.Mapping;

/**
 * Created by sai on 8/9/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "offline.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table taskmapping(task_id integer primary key ,taskname text , dtask_id text,dtaskstatus text)");
        sqLiteDatabase.execSQL("create table projects(project_id integer primary key, project_name varchar(100), user_id integer)");
        sqLiteDatabase.execSQL("create table task(task_id integer primary key, priority_id integer, project_name text, task_name text, due_date text ,estimated_time varchar(100),required_time varchar(100),status varchar(10),total_qty text,done_qty text,task_details text,pdf_link varchar(100),dependent_task_id varchar(10),video_link varchar(100),user_id integer,recordedtime varchar(100),s_taskid text)");
        sqLiteDatabase.execSQL("create table pause_reason(id integer primary key autoincrement, pause_reason_id integer,duration varchar(100), user_id text,task_id text)");
        sqLiteDatabase.execSQL("create table qc(id integer primary key, status text, description varchar(100),video_link varchar(100),image_link text, user_id integer,task_id integer ,FOREIGN KEY(task_id) references task(task_id))");

    }


    public boolean isTaskCompleted(String taskid){

        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from task where task_id = '"+taskid+"' and status = 7",null);
        boolean b=false;
        if (cursor.getCount()>0)
        {
            b=true;
        }
        else {
            b=false;
        }
        return  b;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS projects");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS task");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS pause_reason");

    }

    public String getSyncCount() {
        String num = "0";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT count(task_id) FROM task where status in(7,5,6); ", null);

        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {

                    num = c.getString(0);
                    Log.d("dddd", num);
                } while (c.moveToNext());
            }
        }

        return num;
    }
//        sqLiteDatabase.execSQL("create table pause_reason(id integer primary key autoincrement, pause_reason_id integer,duration varchar(100), user_id text,task_id text)");


    public String getSyncCountPause() {
        String num = "0";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT count(id) FROM pause_reason", null);

        if (c.getCount() > 0) {


            num=""+c.getCount();
            Log.d("dsfs",num);
        }

        return num;
    }

    public void addProject(String project_id, String project_name, String user_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_id", project_id);
        contentValues.put("project_name", project_name);
        contentValues.put("user_id", user_id);


        sqLiteDatabase.insert("projects", null, contentValues);
    }


    public String getProjectName(String project_id)

    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from projects where project_id=? ", new String[]{project_id});

        String name = null;
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    name = c.getString(1);
                } while (c.moveToNext());
            }
        }

        return name;

    }


    public List getAll()

    {

        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from projects", null);

        String name = null;
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    name = c.getString(1);
                    arrayList.add(name);
                } while (c.moveToNext());
            }
        }

        return arrayList;

    }

    public List getAlltask()

    {

        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from task", null);

        String name = null;
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    name = c.getString(c.getColumnIndex("task_id"));
                    arrayList.add(name);
                } while (c.moveToNext());
            }
        }

        return arrayList;

    }


    public boolean isProjectExisting(String project_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from projects where project_id=? ", new String[]{project_id});

        boolean exists = (c.getCount() > 0);
        if (c.getCount() > 0) {
            Log.d("DBHELPER", "" + c.getCount());
        }
        sqLiteDatabase.close();

        return exists;
    }

    public void updateProject(String project_id, String project_name, String user_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("project_name", project_name);
        contentValues.put("user_id", user_id);

        sqLiteDatabase.rawQuery("update projects set project_name='" + project_name + "' , user_id='" + user_id + "' where project_id=" + project_id + " ", null);
        sqLiteDatabase.close();

    }


    public void addTask(String due_date,String task_id, String task_name, String project_name, String priority_id, String estimated_time, String required_time, String status, String total_qty, String done_qty, String task_details, String pdf_link, String dependent_task_id, String video_link, String user_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_id", task_id);
        contentValues.put("priority_id", priority_id);
        contentValues.put("task_name", task_name);
        contentValues.put("due_date", due_date);

        contentValues.put("project_name", project_name);
        contentValues.put("estimated_time", estimated_time);
        contentValues.put("required_time", required_time);
        contentValues.put("status", status);
        contentValues.put("total_qty", total_qty);
        contentValues.put("done_qty", done_qty);
        contentValues.put("task_details", task_details);
        contentValues.put("pdf_link", pdf_link);
        contentValues.put("dependent_task_id", dependent_task_id);
        contentValues.put("video_link", video_link);
        contentValues.put("user_id", user_id);


        sqLiteDatabase.insert("task", null, contentValues);
        sqLiteDatabase.close();
    }

    public boolean istaskExisting(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from task where task_id=? ", new String[]{task_id});

        boolean status;
        if (c.getCount() > 0) {
            Log.d("DBHELPER", "" + c.getCount());

            status = true;
        } else {
            status = false;
        }
        sqLiteDatabase.close();

        return status;
    }

    public String getCounttask(){
        String num="0";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
Cursor c=sqLiteDatabase.rawQuery("select * from task",null);
         num=""+c.getCount();

        return num;

    }

    public void updateTask(String due_date,String task_id, String task_name, String project_name, String priority_id, String estimated_time, String required_time, String status, String total_qty, String done_qty, String task_details, String pdf_link, String dependent_task_id, String video_link, String user_id) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("priority_id", priority_id);
        contentValues.put("task_name", task_name);
        contentValues.put("project_name", project_name);
        contentValues.put("estimated_time", estimated_time);
        contentValues.put("required_time", required_time);
        contentValues.put("total_qty", total_qty);
        contentValues.put("done_qty", done_qty);
        contentValues.put("task_details", task_details);
        contentValues.put("pdf_link", pdf_link);
        contentValues.put("dependent_task_id", dependent_task_id);
        contentValues.put("video_link", video_link);
        contentValues.put("user_id", user_id);
        contentValues.put("due_date", due_date);

        sqLiteDatabase.update("task", contentValues, "task_id=" + task_id + "", null);
        //sqLiteDatabase.rawQuery("update task set priority_id='"+priority_id+"'  , video_link='"+video_link+"' , user_id='"+user_id+"' , dependent_task_id='"+dependent_task_id+"' , pdf_link='"+pdf_link+"' , task_details='"+task_details+"' , done_qty='"+done_qty+"' , total_qty='"+total_qty+"' , status='"+status+"' , required_time='"+required_time+"' , task_name='"+task_name+"' , estimated_time='"+estimated_time+"' , projects_id='"+projects_id+"' where task_id="+task_id+" ",null);

    }


    public void updateTaskStatus(String task_id, String status, String done_qty, String recordedtime) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("done_qty", done_qty);
        contentValues.put("recordedtime", recordedtime);


        sqLiteDatabase.update("task", contentValues, "task_id=" + task_id + "", null);

    }

    public void updateTaskStatusQC(String task_id, String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);


        sqLiteDatabase.update("task", contentValues, "task_id=" + task_id + "", null);

    }


    public void updateDTask(String task_id, String dependent_task_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Log.d("thisisdb", task_id);
        Log.d("thisisdb", dependent_task_id);


        sqLiteDatabase.rawQuery("update task set dependent_task_id='" + dependent_task_id + "' where task_id=" + task_id + " ", null);

    }

    public Cursor getTask(String userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from task where user_id=? and status!=7 order by priority_id ", new String[]{userId});

        return c;

    }

    public Cursor getAllTask(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from task where NOT task_id='"+task_id+"'", null);

        return c;

    }


    public Cursor getTaskDetails(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from task where task_id=?", new String[]{task_id});

        return c;


    }


    public Cursor getQC(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from qc where task_id=?", new String[]{task_id});
        sqLiteDatabase.close();

        return c;

    }


    ///qc         sqLiteDatabase.execSQL("create table qc(id integer primary key, status text, descripton varchar(100),video_link varchar(100),image_link text, user_id integer,task_id integer)");


    public void addQC(String id, String status, String description, String video_link, String image_link, String user_id, String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("status", status);
        contentValues.put("description", description);
        contentValues.put("video_link", video_link);
        contentValues.put("image_link", image_link);
        contentValues.put("user_id", user_id);
        contentValues.put("task_id", task_id);


        sqLiteDatabase.insert("qc", null, contentValues);
        sqLiteDatabase.close();
    }

    public boolean isQCExisting(String id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from qc where id=? ", new String[]{id});

        boolean exists = (c.getCount() > 0);
        sqLiteDatabase.close();
        return exists;
    }

    public Cursor getQcByTask(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select task_name,id,qc.status,qc.description,qc.video_link,qc.image_link from qc inner join task on qc.task_id=task.task_id where qc.task_id=? ", new String[]{task_id});

        return c;

    }

    public void updateQC(String id, String status, String description, String video_link, String image_link, String user_id, String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.rawQuery("update qc set status='" + status + "' ,  description='" + description + "' , task_id='" + task_id + "' ,  video_link='" + video_link + "' , image_link='" + image_link + "' ,  user_id='" + user_id + "' where id=" + id + " ", null);
        sqLiteDatabase.close();
    }


    ///////////////////

    public void insertTaskMapping(String task_id, String dtask_id, String taskname, String dtaskstatus) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_id", task_id);
        contentValues.put("dtask_id", dtask_id);
        contentValues.put("taskname", taskname);
        contentValues.put("dtaskstatus", dtaskstatus);

        sqLiteDatabase.insert("taskmapping", null, contentValues);
        sqLiteDatabase.close();

    }

    public boolean isTaskMapId(String task_id) {
        boolean status;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM taskmapping WHERE task_id=" + task_id;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            status = true;
        } else {
            status = false;
        }
        sqLiteDatabase.close();

        return status;
    }

    public void updateTaskMapping(String task_id, String dtask_id, String taskname, String dtaskstatus) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dtask_id", dtask_id);
        contentValues.put("taskname", taskname);
        contentValues.put("dtaskstatus", dtaskstatus);

        sqLiteDatabase.update("taskmapping", contentValues, "task_id=?", new String[]{task_id});
        sqLiteDatabase.close();

    }

    public String getTaskMap(String taskid) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM taskmapping WHERE task_id=" + taskid;
        String data = null;
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {

                    data = c.getString(c.getColumnIndex("dtask_id"));
                    Log.d("dataaaaz", data);


                } while (c.moveToNext());

            }
        }
        sqLiteDatabase.close();
        return data;

    }

    public String getTaskMapName(String taskid) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM taskmapping WHERE task_id=" + taskid;
        String data = null;
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {

                    data = c.getString(c.getColumnIndex("taskname"));


                } while (c.moveToNext());

            }
        }
        sqLiteDatabase.close();

        return data;

    }

    public void updateTaskData(String task_id, String done_qty, String recordedtime) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("done_qty", done_qty);
        contentValues.put("recordedtime", recordedtime);


        sqLiteDatabase.update("task", contentValues, "task_id=" + task_id + "", null);


    }

    public void updateTask(String task_id, String taskname, String taskdescription, String pdf_link, String d_id, String video_link) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_name", taskname);
        contentValues.put("task_details", taskdescription);
        contentValues.put("pdf_link", pdf_link);
        contentValues.put("video_link", video_link);
        Log.d("Dbhleper", task_id);

        sqLiteDatabase.update("task", contentValues, "task_id=?", new String[]{task_id});
        sqLiteDatabase.close();

    }

    public Cursor getAllDataToSync() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from task", null);

        return cursor;

    }

    public Cursor getTaskStatusUnits(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select status,done_qty,recordedtime from task where task_id = ? ", new String[]{task_id});


        return c;
    }

    public String getTaskStatus(String task_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String data = "0";
        Cursor c = sqLiteDatabase.rawQuery("select status from task where task_id = ? ", new String[]{task_id});
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    data = c.getString(c.getColumnIndex("status"));

                } while (c.moveToNext());
            }
        }

        return data;
    }

    ////////////////////////////Pause
    public void addPauseReason(String pause_reason_id, String duration, String user_id, String task_id) {
        //create table pause_reason(id integer primary key autoincrement, pause_reason_id integer,duration varchar(100), user_id text,task_id text)");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pause_reason_id", pause_reason_id);
        contentValues.put("duration", duration);
        contentValues.put("user_id", user_id);
        contentValues.put("task_id", task_id);
        sqLiteDatabase.insert("pause_reason", null, contentValues);
    }

    public Cursor getAllpause() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from pause_reason", null);


        return c;
    }

    public  ArrayList<Mapping> getdataMapping(String task_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //task_id integer primary key ,taskname text , dtask_id text,dtaskstatus text
        String data="",dtask_id="",t_id="";
        ArrayList<Mapping> al=new ArrayList();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from taskmapping WHERE task_id = '"+task_id+"' or dtask_id = '"+task_id+"'",null);

        if (cursor.getCount()>0)
        {

            if (cursor.moveToFirst()){
                do {
                    t_id=cursor.getString(cursor.getColumnIndex("task_id"));
                    dtask_id=cursor.getString(cursor.getColumnIndex("dtask_id"));
                    Mapping mapping=new Mapping(t_id,dtask_id);
                    al.add(mapping);
                }
                while (cursor.moveToNext());
            }
        }


        return al;
    }

    public  ArrayList<CharSequence> getdataMapping(String task_id,String task_id2)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //task_id integer primary key ,taskname text , dtask_id text,dtaskstatus text
        String data="",dtask_id="",t_id="";
        ArrayList<CharSequence> al=new ArrayList();
        Cursor cursor=sqLiteDatabase.rawQuery("select task_id from task WHERE NOT task_id = '"+task_id+"' and NOT task_id = '"+task_id2+"'",null);

        if (cursor.getCount()>0)
        {

            if (cursor.moveToFirst()){
                do {
                    t_id=cursor.getString(cursor.getColumnIndex("task_id"));

                    al.add(t_id);
                }
                while (cursor.moveToNext());
            }
        }


        return al;
    }

    public  ArrayList<CharSequence> getAllMapping(String task_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //task_id integer primary key ,taskname text , dtask_id text,dtaskstatus text
        String data="",dtask_id="",t_id="";
        ArrayList<CharSequence> al=new ArrayList();
        Cursor cursor=sqLiteDatabase.rawQuery("select task_id from task where NOT task_id ='"+task_id+"'",null);

        if (cursor.getCount()>0)
        {

            if (cursor.moveToFirst()){
                do {
                    t_id=cursor.getString(cursor.getColumnIndex("task_id"));

                    al.add(t_id);
                }
                while (cursor.moveToNext());
            }
        }


        return al;
    }


}
