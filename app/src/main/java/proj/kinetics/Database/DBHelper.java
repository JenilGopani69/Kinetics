package proj.kinetics.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

sqLiteDatabase.execSQL("create table taskmapping(task_id integer primary key ,taskname text , dtask_id text)");
        sqLiteDatabase.execSQL("create table projects(project_id integer primary key, project_name varchar(100), user_id integer)");
        sqLiteDatabase.execSQL("create table task(task_id integer primary key, priority_id integer, projects_id integer, task_name text, estimated_time varchar(100),required_time varchar(100),status varchar(10),total_qty integer,done_qty integer,task_details text,pdf_link varchar(100),dependent_task_id varchar(10),video_link varchar(100),user_id integer,recordedtime varchar(100),s_taskid text)");
        sqLiteDatabase.execSQL("create table pause_reason(id integer primary key, pause_reason_id integer, start_time varchar(100),end_time varchar(100),duration text, user_id integer,task_id integer)");
        sqLiteDatabase.execSQL("create table qc(id integer primary key, status text, descripton varchar(100),video_link varchar(100),image_link text, user_id integer,task_id integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS projects");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS task");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS pause_reason");

    }

    public void addProject(String project_id,String project_name,String user_id){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("project_id",project_id);
        contentValues.put("project_name",project_name);
        contentValues.put("user_id",user_id);


        sqLiteDatabase.insert("projects",null,contentValues);

    }

    public String getProjectName(String project_id)

    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from projects where project_id=? ",new String[]{project_id});

        String name = null;
        if (c.getCount()>0){
            if (c.moveToFirst()){
                do {
                    name=c.getString(1);
                }while (c.moveToNext());
            }
        }

        return name;

    }

    public boolean isProjectExisting(String project_id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from projects where project_id=? ",new String[]{project_id});

        boolean exists = (c.getCount() > 0);
        return exists;
    }

    public void updateProject(String project_id,String project_name,String user_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("project_name",project_name);
        contentValues.put("user_id",user_id);

        sqLiteDatabase.rawQuery("update projects set project_name='"+project_name+"' , user_id='"+user_id+"' where project_id="+project_id+" ",null);


    }



    public void addTask(String task_id,String task_name,String projects_id,String priority_id,String estimated_time,String required_time,String status,String total_qty,String done_qty,String task_details,String pdf_link,String dependent_task_id,String video_link,String user_id){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("task_id",task_id);
        contentValues.put("priority_id",priority_id);
        contentValues.put("task_name",task_name);

        contentValues.put("projects_id",projects_id);
        contentValues.put("estimated_time",estimated_time);
        contentValues.put("required_time",required_time);
        contentValues.put("status",status);
        contentValues.put("total_qty",total_qty);
        contentValues.put("done_qty",done_qty);
        contentValues.put("task_details",task_details);
        contentValues.put("pdf_link",pdf_link);
        contentValues.put("dependent_task_id",dependent_task_id);
        contentValues.put("video_link",video_link);
        contentValues.put("user_id",user_id);



        sqLiteDatabase.insert("task",null,contentValues);
    }

    public boolean istaskExisting(String task_id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from task where task_id=? ",new String[]{task_id});

        boolean status;
        if(c.getCount()>0){
            status=true;
        }else{
            status=false;
        }
        return status;
    }


    public void updateTask(String task_id,String task_name,String projects_id,String priority_id,String estimated_time,String required_time,String status,String total_qty,String done_qty,String task_details,String pdf_link,String dependent_task_id,String video_link,String user_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();





        ContentValues contentValues=new ContentValues();
        contentValues.put("status",status);
        contentValues.put("priority_id",priority_id);
        contentValues.put("task_name",task_name);

        contentValues.put("projects_id",projects_id);
        contentValues.put("estimated_time",estimated_time);
        contentValues.put("required_time",required_time);
        contentValues.put("status",status);
        contentValues.put("total_qty",total_qty);
        contentValues.put("done_qty",done_qty);
        contentValues.put("task_details",task_details);
        contentValues.put("pdf_link",pdf_link);
        contentValues.put("dependent_task_id",dependent_task_id);
        contentValues.put("video_link",video_link);
        contentValues.put("user_id",user_id);

        sqLiteDatabase.update("task",contentValues,"task_id=?",new String[]{task_id});
        //sqLiteDatabase.rawQuery("update task set priority_id='"+priority_id+"'  , video_link='"+video_link+"' , user_id='"+user_id+"' , dependent_task_id='"+dependent_task_id+"' , pdf_link='"+pdf_link+"' , task_details='"+task_details+"' , done_qty='"+done_qty+"' , total_qty='"+total_qty+"' , status='"+status+"' , required_time='"+required_time+"' , task_name='"+task_name+"' , estimated_time='"+estimated_time+"' , projects_id='"+projects_id+"' where task_id="+task_id+" ",null);

    }



    public void updateTaskStatus(String task_id,String status){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();




        ContentValues contentValues=new ContentValues();
        contentValues.put("status",status);


        sqLiteDatabase.update("task",contentValues,"task_id=?",new String[]{task_id});
    }


    public void updateDTask(String task_id,String dependent_task_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        Log.d("thisisdb",task_id);
        Log.d("thisisdb",dependent_task_id);


        sqLiteDatabase.rawQuery("update task set dependent_task_id='"+dependent_task_id+"' where task_id="+task_id+" ",null);

    }
    public Cursor getTask(String userId){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from task where user_id=? and status!=7 order by priority_id ",new String[]{userId});


        return c;

    }


    public Cursor getTaskDetails(String task_id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from task where task_id=?",new String[]{task_id});


        return c;

    }


    public Cursor getQC(String task_id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from qc where task_id=?",new String[]{task_id});


        return c;

    }


    ///qc         sqLiteDatabase.execSQL("create table qc(id integer primary key, status text, descripton varchar(100),video_link varchar(100),image_link text, user_id integer,task_id integer)");


    public void addQC(String id,String status,String description,String video_link,String image_link, String user_id,String task_id)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",id);
        contentValues.put("status",status);
        contentValues.put("description",description);
        contentValues.put("video_link",video_link);
        contentValues.put("image_link",image_link);
        contentValues.put("user_id",user_id);
        contentValues.put("task_id",task_id);


        sqLiteDatabase.insert("qc",null,contentValues);

    }

    public boolean isQCExisting(String id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from qc where id=? ",new String[]{id});

        boolean exists = (c.getCount() > 0);
        return exists;
    }


    public void updateQC(String id,String status,String description,String video_link,String image_link, String user_id,String task_id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();


        sqLiteDatabase.rawQuery("update qc set status='"+status+"' ,  description='"+description+"' , task_id='"+task_id+"' ,  video_link='"+video_link+"' , image_link='"+image_link+"' ,  user_id='"+user_id+"' where id="+id+" ",null);

    }


    ///////////////////

    public void insertTaskMapping(String task_id,String dtask_id, String taskname)
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("task_id",task_id);
        contentValues.put("dtask_id",dtask_id);
        contentValues.put("taskname",taskname);

        sqLiteDatabase.insert("taskmapping",null,contentValues);


    }

    public  boolean isTaskMapId(String task_id){
        boolean status;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sql ="SELECT * FROM taskmapping WHERE task_id="+task_id;

        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);

        if(cursor.getCount()>0){
             status=true;
        }else{
            status=false;
        }

return status;
    }
    public void updateTaskMapping(String task_id,String dtask_id,String taskname )
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("dtask_id",dtask_id);
        contentValues.put("taskname",taskname);

       sqLiteDatabase.update("taskmapping",contentValues,"task_id=?",new String[]{task_id});


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


                } while (c.moveToNext());

            }
        }

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

        return data;

    }

    public void updateTaskData(String task_id,String done_qty,String recordedtime )
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("done_qty",done_qty);
        contentValues.put("recordedtime",recordedtime);

        sqLiteDatabase.update("task",contentValues,"task_id=?",new String[]{task_id});


    }

}
