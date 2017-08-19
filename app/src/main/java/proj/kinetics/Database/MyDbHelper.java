package proj.kinetics.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sai on 12/8/17.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kinetic.db";
    private static final String TABLE_NAME = "tasks";
    private static final int DATABASE_VERSION = 1;






    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `projects_id` int(11) DEFAULT NULL,\n" +
                "  `details` text,\n" +
                "  `estimated_time` varchar(10) DEFAULT NULL,\n" +
                "  `start_datetime` datetime DEFAULT NULL,\n" +
                "  `end_datetime` datetime DEFAULT NULL,\n" +
                "  `duration` varchar(10) DEFAULT NULL,\n" +
                "  `quantity` int(11) DEFAULT NULL,\n" +
                "  `amount` varchar(45) DEFAULT NULL,\n" +
                "  `assigned_to` int(11) DEFAULT NULL,\n" +
                "  `status` varchar(1) DEFAULT NULL,\n" +
                "  `assigned_datetime` datetime DEFAULT NULL,\n" +
                "  `attachment_link` text,\n" +
                "  `video_link` text,\n" +
                "  `audit_created_date` datetime DEFAULT NULL,\n" +
                "  `audit_updated_date` datetime DEFAULT NULL,\n" +
                "  `audit_updated_by` int(11) DEFAULT NULL,\n" +
                "  `audit_created_by` int(11) DEFAULT NULL,\n" +
                "  `audit_ip` varchar(45) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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
}
