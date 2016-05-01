package net.jmesh.localist;

import android.location.Location;

/**
 * Created by pluxsuwong on 4/30/16.
 */
public class LocSingleton {
    private static Location loc;

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }
}
