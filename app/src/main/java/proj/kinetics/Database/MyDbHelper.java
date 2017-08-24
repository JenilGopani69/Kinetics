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
    private static final int DATABASE_VERSION = 1;






    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL("create table usertaskjson(id integer primary key autoincrement,jsondata varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME, null, null);

    }
}
