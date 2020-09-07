package com.bk.bicycletracker;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class TimespanTest {

    @Test
    public void getTimespanForDay() {
        Calendar calendar_in = Calendar.getInstance();
        Timespan dut = new Timespan();
        dut.getTimespanForDay(calendar_in);
        Calendar calendar_out_min = Calendar.getInstance();
        Calendar calendar_out_max = Calendar.getInstance();

        calendar_out_min.setTimeInMillis(dut.getUnixTimeMin()*1000);
        calendar_out_max.setTimeInMillis(dut.getUnixTimeMax()*1000);

        assertEquals(calendar_out_min.get(Calendar.SECOND), 0);
        assertEquals(calendar_out_min.get(Calendar.MINUTE), 0);
        assertEquals(calendar_out_min.get(Calendar.HOUR_OF_DAY), 0);

        assertEquals(calendar_out_max.get(Calendar.SECOND), 59);
        assertEquals(calendar_out_max.get(Calendar.MINUTE), 59);
        assertEquals(calendar_out_max.get(Calendar.HOUR_OF_DAY), 23);

        assertEquals(calendar_out_max.get(Calendar.DATE), calendar_out_min.get(Calendar.DATE));
    }

    @Test
    public void getTimespanForWeek() {
        Calendar calendar_in = Calendar.getInstance();
        Timespan dut = new Timespan();
        dut.getTimespanForWeek(calendar_in);
        Calendar calendar_out_min = Calendar.getInstance();
        Calendar calendar_out_max = Calendar.getInstance();

        long unixtime_min =  dut.getUnixTimeMin();
        long unixtime_max =  dut.getUnixTimeMax();


        calendar_out_min.setTimeInMillis(unixtime_min*1000);
        calendar_out_max.setTimeInMillis(unixtime_max*1000);

        assertEquals(calendar_out_min.get(Calendar.SECOND), 0);
        assertEquals(calendar_out_min.get(Calendar.MINUTE), 0);
        assertEquals(calendar_out_min.get(Calendar.HOUR_OF_DAY), 0);

        assertEquals(calendar_out_max.get(Calendar.SECOND), 59);
        assertEquals(calendar_out_max.get(Calendar.MINUTE), 59);
        assertEquals(calendar_out_max.get(Calendar.HOUR_OF_DAY), 23);

        assertEquals(calendar_out_min.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
        assertEquals(calendar_out_max.get(Calendar.DAY_OF_WEEK), Calendar.SUNDAY);

        assertEquals(7*24*60*60-1, unixtime_max-unixtime_min );
    }
}