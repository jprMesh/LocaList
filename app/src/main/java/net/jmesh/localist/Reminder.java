package net.jmesh.localist;

import android.location.Location;

import java.util.Date;
import java.util.UUID;

public class Reminder {

    private UUID mId;
    private String mTitle;
    private String mType;
    private String mContent;
    private double mLatitude;
    private double mLongitude;
    private Date mDate;

    public Reminder() {
        this(UUID.randomUUID());
    }

    public Reminder(UUID id) {
        mId = id;
        mDate = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Location getLocation() {
        Location aLoc = new Location("dummyprovider");
        aLoc.setLatitude(mLatitude);
        aLoc.setLongitude(mLongitude);
        return aLoc;
    }

    public void setLocation(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }
}
