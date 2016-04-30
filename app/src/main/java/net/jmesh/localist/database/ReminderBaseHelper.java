package net.jmesh.localist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.jmesh.localist.database.ReminderDbSchema.NoteTable;
import net.jmesh.localist.database.ReminderDbSchema.ListTable;

public class ReminderBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ReminderBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "reminderBase.db";

    public ReminderBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + NoteTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    NoteTable.Cols.UUID + ", " +
                    NoteTable.Cols.TITLE + ", " +
                    NoteTable.Cols.CONTENT + ", " +
                    NoteTable.Cols.LATITUDE + ", " +
                    NoteTable.Cols.LONGITUDE + ", " +
                    NoteTable.Cols.DATE +
                    ");"
        );

        db.execSQL("create table " + ListTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    ListTable.Cols.UUID + ", " +
                    ListTable.Cols.TITLE + ", " +
                    ListTable.Cols.CONTENT + ", " +
                    ListTable.Cols.ACTIVITY +
                    ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ListTable.NAME);
        onCreate(db);
    }
}
