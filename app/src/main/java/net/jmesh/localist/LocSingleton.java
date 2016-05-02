package net.jmesh.localist;

import android.location.Location;

/**
 * Created by pluxsuwong on 4/30/16.
 */
public class LocSingleton {
    private static double lat;
    private static double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() { return lon; }

    public void setLocation(double lat, double lon) {
        this.lat = lat; this.lon = lon;
    }
}
