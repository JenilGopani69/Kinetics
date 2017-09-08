package proj.kinetics.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sai on 12/8/17.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kinetic.db";
    private static final String TABLE_NAME = "usertaskjson";
    private static final String TABLE_NAME2 = "taskdetail";
    private static final String TABLE_NAME3 = "updatetasktimer";
    private static final String TABLE_NAME4 = "pausedetails";
    private static final String TABLE_NAME5 = "dependenttask";
    private static final String TABLE_NAME6 = "qc";
    private static final int DATABASE_VERSION = 1;






    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL("create table usertaskjson(userid integer primary key,jsondata varchar(1000))");
       sqLiteDatabase.execSQL("create table taskdetail(taskid integer primary key,jsontaskdetail varchar(1000))");

        //update task timer

        sqLiteDatabase.execSQL("create table updatetasktimer(id integer primary key autoincrement,user_id text,task_id text,amount text,start_time text,stop_time text)");
        sqLiteDatabase.execSQL("create table pausedetails(id integer primary key autoincrement,user_id text,task_id text,pause_id text,pause_time text)");


        //qc update
        sqLiteDatabase.execSQL("create table qc(taskid integer primary key,userid text, qcstatus text)");


        //update dependent task timer
        sqLiteDatabase.execSQL("create table dependenttask(taskid integer primary key,userid text,dtaskid text,amount text,duration text)");






    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        onCreate(sqLiteDatabase);
    }
    public boolean isRecordExists(String columnName, String tableName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!db.isOpen()) this.onOpen(db);
        Cursor cursor = db.rawQuery("select * from " + tableName + " where " + columnName + "=?",
                new String[]{id});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public  void insertData(String userid,String jsondata){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("jsondata",jsondata);
        contentValues.put("userid",userid);
            sqLiteDatabase.insert(TABLE_NAME,null,contentValues);


    }
    public  void updateData(String jsondata,String userid){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("jsondata",jsondata);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"userid=?",new String[]{userid});


    }
    public boolean isDataExists(String user_id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where userid=?",new String[]{user_id});

        boolean exists = (c.getCount() > 0);
        return exists;

    }

    public Cursor getData(String userId){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where userId=?",new String[]{userId});


        return c;

    }



    //task details offline



    public  void insertTaskData(String jsondata,String taskid){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("jsontaskdetail",jsondata);
        contentValues.put("taskid",taskid);
        sqLiteDatabase.insert(TABLE_NAME2,null,contentValues);


    }


    public boolean isTaskDataExists(String taskid){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME2+" where taskid=?",new String[]{taskid});

        boolean exists = (c.getCount() > 0);
        return exists;

    }

    public boolean isUserDataExists(String userId){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME2+" where user_id=?",new String[]{userId});

        boolean exists = (c.getCount() > 0);
        return exists;

    }
    public Cursor getTaskDataBytaskid(String taskid) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME2+" where taskid=?", new String[]{taskid});


        return c;
    }

    public Cursor getTaskDataByuser_id(String user_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME2+" where user_id=?", new String[]{user_id});


        return c;
    }

    public  void updateTaskData(String jsondata,String taskid){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("jsontaskdetail",jsondata);
        contentValues.put("taskid",taskid);
        sqLiteDatabase.update(TABLE_NAME2,contentValues,"taskid=?",new String[]{taskid});


    }



    /////Update task time

    public  void insertTaskTimer(String user_id,String task_id,String amount,String start_time,String stop_time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("user_id",user_id);
        contentValues.put("task_id",task_id);
        contentValues.put("amount",amount);
        contentValues.put("start_time",start_time);
        contentValues.put("stop_time",stop_time);
        sqLiteDatabase.insert(TABLE_NAME3,null,contentValues);


    }
    public Cursor getTaskTimerDetails(String task_id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME3+" where task_id=?", new String[]{task_id});


        return c;
    }

    public  void updateTaskTimer(String user_id,String task_id,String amount,String start_time,String stop_time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("user_id",user_id);
        contentValues.put("amount",amount);
        contentValues.put("start_time",start_time);
        contentValues.put("stop_time",stop_time);
        sqLiteDatabase.update(TABLE_NAME3,contentValues,"task_id=?",new String[]{task_id});


    }
    public boolean isTaskTimerExists(String taskid){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME3+" where task_id=?",new String[]{taskid});

        boolean exists = (c.getCount() > 0);
        return exists;

    }

    /////pause time



    public  void insertPause(String user_id,String task_id,String pause_id,String pause_time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("user_id",user_id);
        contentValues.put("task_id",task_id);
        contentValues.put("pause_id",pause_id);
        contentValues.put("pause_time",pause_time);
        sqLiteDatabase.insert(TABLE_NAME4,null,contentValues);


    }

   //similar task
          //  sqLiteDatabase.execSQL("create table dependenttask(taskid integer primary key,userid text,dtaskid text,amount text,duration text)");


    public void insertDependentTask(String userid,String taskid,String dtaskid)
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("userid",userid);
        contentValues.put("taskid",taskid);
        contentValues.put("dtaskid",dtaskid);
        sqLiteDatabase.insert(TABLE_NAME5,null,contentValues);

    }

    public void updateDependentTask(String dtaskid,String amount,String duration)
    {
    SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    ContentValues contentValues=new ContentValues();

    contentValues.put("dtaskid",dtaskid);
    contentValues.put("amount",amount);
    contentValues.put("duration",duration);

    sqLiteDatabase.update(TABLE_NAME5,contentValues,"dtaskid=?",new String[]{dtaskid});
    }

        


    public void insertqcstatus(String taskid,String userid,String qcstatus)
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("taskid",taskid);
        contentValues.put("userid",userid);
        contentValues.put("qcstatus",qcstatus);
        sqLiteDatabase.insert(TABLE_NAME6,null,contentValues);



    }



}
