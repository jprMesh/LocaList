package net.jmesh.localist;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import net.jmesh.localist.database.ReminderDataBase;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {

    private SQLiteDatabase mDatabase;
    private ReminderDataBase rDatabase;

    private int highestConfidence;
    private int ourActivity;
    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        if (rDatabase == null) {
            rDatabase = new ReminderDataBase();
            mDatabase = rDatabase.getDB(this);
        }
        highestConfidence = 0;
        Intent activityIntent = new Intent();
        activityIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESP);
        activityIntent.addCategory(Intent.CATEGORY_DEFAULT);
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        ourActivity = 0;
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        ourActivity = 1;
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        ourActivity = 2;
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    /*if( activity.getConfidence() >= 75 ) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText( "Are you walking?" );
                        builder.setSmallIcon( R.mipmap.ic_launcher );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }*/
                    if (activity.getConfidence() > highestConfidence) {
                        ourActivity = 3;
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                default: {
                    //do nothing
                    break;
                }
            }
        }
        String tmpStr;
        if (ourActivity == 0) {
            tmpStr = "In Vehicle";
            activityIntent.putExtra("ACTION", "You are in a vehicle");
        } else if (ourActivity == 1) {
            tmpStr = "Running";
            activityIntent.putExtra("ACTION", "You are running");
        } else if (ourActivity == 2) {
            tmpStr = "Sitting";
            activityIntent.putExtra("ACTION", "You are still");
        } else if (ourActivity == 3) {
            tmpStr = "Walking";
            activityIntent.putExtra("ACTION", "You are walking");
        } else {
            tmpStr = "";
            activityIntent.putExtra("ACTION", "Initializing...");
        }
        sendBroadcast(activityIntent);

        List<ReminderNote> dbEntries = new ArrayList<ReminderNote>();
        String[] columns = new String[]{"uuid", "title",
                "content", "activity"};
        String [] args = {tmpStr};
        Cursor cursor = mDatabase.rawQuery("select * from lists where activity = ?", args);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ReminderNote newNote = new ReminderNote();
            newNote.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            newNote.setContent(cursor.getString(cursor.getColumnIndex("content")));
            newNote.setDate(new Date(cursor.getLong(cursor.getColumnIndex("activity"))));
            dbEntries.add(newNote);
            cursor.moveToNext();
        }
        Notification.Builder nb = new Notification.Builder(this);
        nb.setSmallIcon(R.drawable.ic_star_black_36dp);
        nb.setContentTitle("LocaList Reminder");
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("fromnotif", 0);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);
        nb.setContentIntent(intent);
        if (dbEntries.size() > 0) {
            nb.setContentText(dbEntries.get(0).getTitle() + ". (You are " + tmpStr + ").");
            Notification notification = nb.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, notification);
        }

    }
}
