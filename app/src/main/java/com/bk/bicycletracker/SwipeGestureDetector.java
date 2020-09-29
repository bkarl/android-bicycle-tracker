package com.bk.bicycletracker;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.Calendar;

public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
    Calendar currentWeek = Calendar.getInstance();
    TimeDependentFragment callbackFragment;
    private static final String TAG = "SwipeGestureDetector";


    public SwipeGestureDetector(TimeDependentFragment callbackFragment)
    {
        this.callbackFragment = callbackFragment;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        final int SWIPE_MIN_DISTANCE = 120;
        final int SWIPE_MAX_OFF_PATH = 250;
        final int SWIPE_THRESHOLD_VELOCITY = 200;
        try {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                Calendar now = Calendar.getInstance();
                now.set(Calendar.DAY_OF_WEEK, now.getActualMaximum(Calendar.DAY_OF_WEEK));
                //Log.i(TAG, "down up "+currentWeek.get(Calendar.WEEK_OF_YEAR)+" < "+now.get(Calendar.WEEK_OF_YEAR));
                if (currentWeek.get(Calendar.WEEK_OF_YEAR) < now.get(Calendar.WEEK_OF_YEAR)) {
                    currentWeek.add(Calendar.DAY_OF_YEAR, +7);
                }

            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                currentWeek.add(Calendar.DAY_OF_YEAR, -7);
                //Log.i(TAG, "up down "+currentWeek.toString());
            }
        } catch (Exception e) {
            // nothing
            Log.i(TAG, e.toString());
        }
        callbackFragment.dateChanged(currentWeek);
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
