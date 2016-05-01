package net.jmesh.localist.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by pluxsuwong on 4/30/16.
 */
public class ReminderDataBase {
    private ReminderBaseHelper mDatabaseHelper;
    private static SQLiteDatabase mDatabase;

    public SQLiteDatabase getDB(Context c) {
        if (mDatabase == null) {
            mDatabaseHelper = new ReminderBaseHelper(c);
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

}
