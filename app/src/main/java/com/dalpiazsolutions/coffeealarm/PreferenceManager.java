package com.dalpiazsolutions.coffeealarm;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private Context context;
    private SharedPreferences prefsTime;
    private static final String PREFS_TIME = "timeFile";


    public PreferenceManager(Context context)
    {
        this.context = context;
    }

    public void start()
    {
        prefsTime = context.getSharedPreferences(PREFS_TIME, Context.MODE_PRIVATE);
    }

    public void writeAlarmTime(int hour, int minute)
    {
        SharedPreferences.Editor editTime = prefsTime.edit();
        editTime.putInt("hour", hour);
        editTime.putInt("minute", minute);
        editTime.apply();
    }

    public int[] getTime()
    {
        int[] time = new int[2];
        time[0] = prefsTime.getInt("hour", -1);
        time[1] = prefsTime.getInt("minute", -1);
        return time;
    }

    public void writeInterval(boolean interval)
    {
        SharedPreferences.Editor editTime = prefsTime.edit();
        editTime.putBoolean("interval", interval);
        editTime.apply();
    }

    public boolean getInterval()
    {
        return prefsTime.getBoolean("interval", false);
    }
}
