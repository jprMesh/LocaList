package net.jmesh.localist;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import net.jmesh.localist.PageFragment;
import net.jmesh.localist.database.ReminderBaseHelper;
import net.jmesh.localist.database.ReminderDataBase;
import net.jmesh.localist.database.ReminderDbSchema;
import net.jmesh.localist.database.ReminderDbSchema.NoteTable;
import net.jmesh.localist.database.ReminderDbSchema.ListTable;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    OnMapReadyCallback, LocationListener {

    public GoogleApiClient mApiClient;
    private ResponseReceiver mReceiver;
    private SQLiteDatabase mDatabase;
    public GoogleMap mMap;
    public Location mLastLocation;
    public LocationManager mLocationManager;
    private ReminderDataBase rDatabase;
    private LocSingleton curLoc;
    private String activityField = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rDatabase = new ReminderDataBase();
        mDatabase = rDatabase.getDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                    MainActivity.this));
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingtabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pageID = viewPager.getCurrentItem();

                    if (mLastLocation == null) {
                        mLastLocation = new Location("dummyprovider");
                        mLastLocation.setLatitude(0);
                        mLastLocation.setLongitude(0);
                    }
                    if (pageID == 0) {
                        EditText titletext = (EditText)findViewById(R.id.titlefieldnote);
                        EditText bodytext = (EditText)findViewById(R.id.bodytextfield);
                        ContentValues values = new ContentValues();
                        values.put(NoteTable.Cols.UUID, 0);  // update somehow
                        values.put(NoteTable.Cols.TITLE, titletext.getText().toString());
                        values.put(NoteTable.Cols.CONTENT, bodytext.getText().toString());
                        values.put(NoteTable.Cols.LATITUDE, mLastLocation.getLatitude());
                        values.put(NoteTable.Cols.LONGITUDE, mLastLocation.getLongitude());
                        Calendar c = Calendar.getInstance();
                        long seconds = c.get(Calendar.SECOND);
                        values.put(NoteTable.Cols.DATE, seconds);
                        mDatabase.insert(NoteTable.NAME, null, values);
                        long dbSize = getEntryCnt(NoteTable.NAME);
                        String printMsg = titletext.getText().toString() + "\nYou now have " + dbSize + " note entries";
                        Snackbar.make(view, printMsg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else if (pageID == 1) {
                        EditText titletext = (EditText)findViewById(R.id.titlefieldlist);
                        ContentValues values = new ContentValues();
                        values.put(ListTable.Cols.UUID, 0);  // update somehow
                        values.put(ListTable.Cols.TITLE, titletext.getText().toString());
                        values.put(ListTable.Cols.CONTENT, "");
                        values.put(ListTable.Cols.ACTIVITY, activityField);
                        mDatabase.insert(ListTable.NAME, null, values);
                        long dbSize = getEntryCnt(ListTable.NAME);
                        String printMsg = activityField + "\nYou now have " + dbSize + " list entries";
                        Snackbar.make(view, printMsg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }

    private long getEntryCnt(String table) {
        return DatabaseUtils.queryNumEntries(mDatabase, table);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 1000, pendingIntent);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        }
        try {
            mLocationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 30, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "name.heqian.cs528.googlefit.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("ACTION");
        }
    }

    protected void onStart() {
        mApiClient.connect();
        super.onStart();


    }

    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Location tmpLoc = new Location("dummyprovider");
        tmpLoc.setLatitude(0);
        tmpLoc.setLongitude(0);
        if (location == null) {
            tmpLoc.setLatitude(0);
            tmpLoc.setLongitude(0);
        } else {
            tmpLoc = location;
        }
        curLoc.setLocation(tmpLoc);
        mLastLocation = tmpLoc;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void setActivityField(String activity) {
        activityField = activity;
    }

}
