package net.jmesh.localist;

import android.location.Location;

import java.util.Date;
import java.util.UUID;

public class ReminderList {

    private UUID mId;
    private String mTitle;
    private String mContent;
    private String mActivity;

    public ReminderList() {
        this(UUID.randomUUID());
    }

    public ReminderList(UUID id) {
        mId = id;
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

    public String getActivity() {
        return mActivity;
    }

    public void setActivity(String activity) {
        mActivity = activity;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

}
