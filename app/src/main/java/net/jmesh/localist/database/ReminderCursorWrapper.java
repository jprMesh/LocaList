package net.jmesh.localist.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;
import android.util.Log;

import net.jmesh.localist.Reminder;

import java.util.Date;
import java.util.UUID;

import net.jmesh.localist.database.ReminderDbSchema.ReminderTable;

public class ReminderCursorWrapper extends CursorWrapper {
    public ReminderCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Reminder getReminder() {
        String uuidString = getString(getColumnIndex(ReminderTable.Cols.UUID));
        String title = getString(getColumnIndex(ReminderTable.Cols.TITLE));
        String type = getString(getColumnIndex(ReminderTable.Cols.TYPE));
        String content = getString(getColumnIndex(ReminderTable.Cols.CONTENT));
        double latitude = getDouble(getColumnIndex(ReminderTable.Cols.LATITUDE));
        double longtitude = getDouble(getColumnIndex(ReminderTable.Cols.LONGTITUDE));
        Location location = new Location("dummyprovider");
        location.setLatitude(latitude);
        location.setLongitude(longtitude);
        long date = getLong(getColumnIndex(ReminderTable.Cols.DATE));

        Reminder reminder = new Reminder(UUID.fromString(uuidString));
        reminder.setTitle(title);
        reminder.setType(type);
        reminder.setContent(content);
        reminder.setLocation(location);
        reminder.setDate(new Date(date));

        return reminder;
    }
}
