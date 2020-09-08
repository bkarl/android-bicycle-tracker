package com.bk.bicycletracker.DatabaseOperations;

import java.util.Calendar;

public class Timespan {

    private long unixTimeMin;
    private long unixTimeMax;

    public long getUnixTimeMin() {
        return unixTimeMin;
    }

    public long getUnixTimeMax() {
        return unixTimeMax;
    }

    public void getTimespanForDay(Calendar day){
        //calculate day span
        day.set(Calendar.SECOND, day.getActualMinimum(Calendar.SECOND));
        day.set(Calendar.MINUTE, day.getActualMinimum(Calendar.MINUTE));
        day.set(Calendar.HOUR_OF_DAY, day.getActualMinimum(Calendar.HOUR_OF_DAY));

        this.unixTimeMin = day.getTimeInMillis()/1000;

        day.set(Calendar.SECOND, day.getActualMaximum(Calendar.SECOND));
        day.set(Calendar.MINUTE, day.getActualMaximum(Calendar.MINUTE));
        day.set(Calendar.HOUR_OF_DAY, day.getActualMaximum(Calendar.HOUR_OF_DAY));

        this.unixTimeMax = day.getTimeInMillis()/1000;
    }

    public void getTimespanForWeek(Calendar dayOfWeek){
        dayOfWeek.setFirstDayOfWeek(Calendar.MONDAY); //TODO: set according to locale

        //calculate day span
        dayOfWeek.set(Calendar.SECOND, dayOfWeek.getActualMinimum(Calendar.SECOND));
        dayOfWeek.set(Calendar.MINUTE, dayOfWeek.getActualMinimum(Calendar.MINUTE));
        dayOfWeek.set(Calendar.HOUR_OF_DAY, dayOfWeek.getActualMinimum(Calendar.HOUR_OF_DAY));
        dayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        unixTimeMin = dayOfWeek.getTimeInMillis()/1000;

        dayOfWeek.set(Calendar.SECOND, dayOfWeek.getActualMaximum(Calendar.SECOND));
        dayOfWeek.set(Calendar.MINUTE, dayOfWeek.getActualMaximum(Calendar.MINUTE));
        dayOfWeek.set(Calendar.HOUR_OF_DAY, dayOfWeek.getActualMaximum(Calendar.HOUR_OF_DAY));
        dayOfWeek.add(Calendar.DATE, 6);

        unixTimeMax = dayOfWeek.getTimeInMillis()/1000;
    }
}