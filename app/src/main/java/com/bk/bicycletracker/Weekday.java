package com.bk.bicycletracker;

public enum Weekday {

    MONDAY("mon", R.id.txtBiasKMMon),
    TUESDAY("tue", R.id.txtBiasKMTue),
    WEDNESDAY("wed", R.id.txtBiasKMWed),
    THURSDAY("thu", R.id.txtBiasKMThu),
    FRIDAY("fri", R.id.txtBiasKMFri),
    SATURDAY("sat", R.id.txtBiasKMSat),
    SUNDAY("sun", R.id.txtBiasKMSun);

    private String suffix;
    private int id;
    private Weekday(String suffix, int id) {
        this.suffix = suffix;
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getRessourceID() {
        return this.id;
    }
}