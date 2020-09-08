package com.bk.bicycletracker;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;

import androidx.test.InstrumentationRegistry;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.bk.bicycletracker.DatabaseOperations.DistanceTracker;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DistanceCalculatorTest {

    private SQLiteDatabase database;
    private Calendar calendarNow;
    private float expectedDistanceDay = 0;
    @Before
    public void setUp() throws Exception {
        TrackDatabaseHelper helper = new TrackDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext(), true);
        database = helper.getWritableDatabase();
        assertNotNull(database);

        DistanceTracker distanceTracker = new DistanceTracker(database);
        calendarNow = Calendar.getInstance();
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
        expectedDistanceDay = result[0];
    }

    @Test
    public void getDistanceForDay() {
        DistanceCalculator distanceCalculator = new DistanceCalculator(database);
        float distance = distanceCalculator.getDistanceForDay(calendarNow);
        assertEquals(expectedDistanceDay, distance,0.001);
    }

    @Test
    public void getDistanceForWeek() {
    }
}