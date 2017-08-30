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
    private static final int DATABASE_VERSION = 1;






    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL("create table usertaskjson(id integer primary key autoincrement,jsondata varchar(1000))");
       sqLiteDatabase.execSQL("create table taskdetail(taskid integer primary key,jsontaskdetail varchar(1000))");

        //update task timer

        sqLiteDatabase.execSQL("create table updatetasktimer(id integer primary key autoincrement,user_id text,task_id text,amount text,start_time text,stop_time text)");
        sqLiteDatabase.execSQL("create table pausedetails(id integer primary key autoincrement,user_id text,task_id text,pause_id text,pause_time text)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
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
    public  void insertData(String jsondata){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("jsondata",jsondata);
            sqLiteDatabase.insert(TABLE_NAME,null,contentValues);


    }
    public boolean isDataExists(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);

        boolean exists = (c.getCount() > 0);
        return exists;

    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);


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
    public Cursor getTaskData(String taskid) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME2+" where taskid=?", new String[]{taskid});


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

    public  void updatetPause(String user_id,String task_id,String pause_id,String pause_time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("user_id",user_id);
        contentValues.put("pause_id",pause_id);
        contentValues.put("pause_time",pause_time);
        sqLiteDatabase.update(TABLE_NAME4,contentValues,"task_id=?",new String[]{task_id});


    }


    /*public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME, null, null);

    }*/
}
