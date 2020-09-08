package com.bk.bicycletracker.DatabaseOperations;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;

import androidx.test.InstrumentationRegistry;

import com.bk.bicycletracker.TrackDataBaseSchema;
import com.bk.bicycletracker.TrackDatabaseHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DistanceTrackerTest {

    private SQLiteDatabase database;
    private Calendar calendarNow;
    private float expectedDistance;
    DistanceTracker distanceTracker;
    @Before
    public void setUp() throws Exception {
        TrackDatabaseHelper helper = new TrackDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext(), true);

        database = helper.getWritableDatabase();
        assertNotNull(database);

        distanceTracker = new DistanceTracker(database);
        assertEquals(1, distanceTracker.getCurrentTrackID());
    }

    @Test
    public void addNewTrackedLocation() {
        calendarNow = Calendar.getInstance();
        addTwoTestLocations();

        assertEquals(expectedDistance, distanceTracker.getDistance(),0.001);

        Cursor c = queryDatabaseForTable(database, TrackDataBaseSchema.LocationEntry.TABLE_NAME);
        assertEquals(2, c.getCount());
    }

    private void addTwoTestLocations() {
        Location locA = new Location(LocationManager.GPS_PROVIDER);
        Location locB = new Location(LocationManager.GPS_PROVIDER);
        locA.setAccuracy(10);
        locA.setLatitude(51.02586);
        locA.setLongitude(13.72355);
        distanceTracker.addNewTrackedLocation(locA);
        locA.setAccuracy(10);
        locB.setLatitude(51.02643);
        locB.setLongitude(13.72026);
        distanceTracker.addNewTrackedLocation(locB);
        float result[] = new float[] {0,0,0};
        Location.distanceBetween(locA.getLatitude(), locA.getLongitude(), locB.getLatitude(), locB.getLongitude(), result);
        expectedDistance = result[0];

    }

    private Cursor queryDatabaseForTable(SQLiteDatabase database, String tableName) {
        return database.query(
                tableName,          // The table to query
                null,       // The columns to return
                null,      // The columns for the WHERE clause
                null,   // The values for the WHERE clause
                null,
                null,
                null);
    }
}