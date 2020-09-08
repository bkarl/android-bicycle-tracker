package com.bk.bicycletracker;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by bk on 07.07.16.
 */
public class LoggingService extends Service {

    private static final String TAG = "ĹocationLogger";
    private static int NOTIFICATION_ID = 25;

    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    LocationListener locationListener;

    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        createNotification();
        initializeLocationManager();
        SQLiteDatabase db = new TrackDatabaseHelper(getApplicationContext()).getWritableDatabase();
        locationListener = new LocationListener(LocationManager.GPS_PROVIDER, db);
        requestLocationUpdates();

        Toast.makeText(this, "service started, trackID " + locationListener.getDistanceTracker().getCurrentTrackID(), Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    private void requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private void createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, getString(R.string.channel_name))
                        .setSmallIcon(R.drawable.launcher)
                        .setContentTitle("Bicycle Tracker")
                        .setContentText("Currently Tracking")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        startForeground(NOTIFICATION_ID, notification);
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroying");

        if (locationManager != null) {
            try {
                if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
                    locationManager.removeUpdates(locationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }
    }
}