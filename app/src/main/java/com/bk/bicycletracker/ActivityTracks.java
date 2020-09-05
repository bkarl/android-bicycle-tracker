package com.bk.bicycletracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityTracks extends AppCompatActivity {

    private TrackDatabaseHelper mDB;
    private double lastLongitude;
    private double lastLatitude;


    private TextView txtDistance;


    private double overallDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        txtDistance = (TextView) findViewById(R.id.txtDistance);

        mDB = new TrackDatabaseHelper(getApplicationContext());
        getLastTrackedLocation();

        txtDistance.setText(Math.round(overallDistance / 10.0)/100.0 + " km");

    }

    private void getLastTrackedLocation()
    {
        SQLiteDatabase db = mDB.getReadableDatabase();
        double longitudeA, latitudeA, longitudeB, latitudeB;


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME + " DESC";

        Cursor c = db.query(
                TrackDataBaseSchema.LocationEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                //TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID+" == 18",                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();

        lastLatitude = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
        lastLongitude = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));

        longitudeA = lastLongitude;
        latitudeA = lastLatitude;

        while (!c.isLast())
        {
            latitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
            longitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));
            float result[] = new float[] {0,0,0};
           // items.add(new OverlayItem("", "", new GeoPoint(latitudeA,longitudeA))); // Lat/Lon decimal degrees

            Location.distanceBetween(latitudeA, longitudeA, latitudeB, longitudeB, result);
            longitudeA = longitudeB;
            latitudeA = latitudeB;
            if (result[0] < 20)
                overallDistance += result[0];
            c.moveToNext();
        }

    }
}
