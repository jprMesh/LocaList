package net.jmesh.localist.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;

import net.jmesh.localist.ReminderList;
import net.jmesh.localist.ReminderNote;

import java.util.Date;
import java.util.UUID;

import net.jmesh.localist.database.ReminderDbSchema.NoteTable;
import net.jmesh.localist.database.ReminderDbSchema.ListTable;

public class ReminderCursorWrapper extends CursorWrapper {
    public ReminderCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ReminderNote getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String content = getString(getColumnIndex(NoteTable.Cols.CONTENT));
        double latitude = getDouble(getColumnIndex(NoteTable.Cols.LATITUDE));
        double longitude = getDouble(getColumnIndex(NoteTable.Cols.LONGITUDE));
        Location location = new Location("dummyprovider");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));

        ReminderNote reminderNote = new ReminderNote(UUID.fromString(uuidString));
        reminderNote.setTitle(title);
        reminderNote.setContent(content);
        reminderNote.setLocation(location);
        reminderNote.setDate(new Date(date));

        return reminderNote;
    }

    public ReminderList getList() {
        String uuidString = getString(getColumnIndex(ListTable.Cols.UUID));
        String title = getString(getColumnIndex(ListTable.Cols.TITLE));
        String content = getString(getColumnIndex(ListTable.Cols.CONTENT));
        String activity = getString(getColumnIndex(ListTable.Cols.ACTIVITY));

        ReminderList list = new ReminderList(UUID.fromString(uuidString));
        list.setTitle(title);
        list.setContent(content);
        list.setActivity(activity);

        return list;
    }
}
