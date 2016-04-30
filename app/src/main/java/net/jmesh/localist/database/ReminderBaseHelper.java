package net.jmesh.localist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.jmesh.localist.database.ReminderDbSchema.ReminderTable;

public class ReminderBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ReminderBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "reminderBase.db";

    public ReminderBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + ReminderTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ReminderTable.Cols.UUID + ", " +
                ReminderTable.Cols.TITLE + ", " +
                ReminderTable.Cols.PAGE + ", " +
                ReminderTable.Cols.CONTENT + ", " +
                ReminderTable.Cols.LATITUDE + ", " +
                ReminderTable.Cols.LONGITUDE + ", " +
                ReminderTable.Cols.DATE +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReminderTable.NAME);
        onCreate(db);
    }
}
