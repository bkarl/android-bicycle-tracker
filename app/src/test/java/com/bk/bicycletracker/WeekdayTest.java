package com.bk.bicycletracker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeekdayTest {

    @Test
    public void getSuffix() {
        Weekday wd = Weekday.SATURDAY;
        Assert.assertEquals("sat", wd.getSuffix());
    }

    @Test
    public void getRessourceID() {
        Weekday wd = Weekday.TUESDAY;
        Assert.assertEquals(R.id.txtBiasKMTue, wd.getRessourceID());
    }
}