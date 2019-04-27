package com.dalpiazsolutions.coffeealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainController {

    private PreferenceManager preferenceManager;
    private HTTPDownloader httpDownloader;
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private int day_of_month;
    private int month;
    private int year;

    public MainController(Context context)
    {
        preferenceManager = new PreferenceManager(context);
        preferenceManager.start();
        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void getCoffee()
    {
        httpDownloader = new HTTPDownloader();
        try {
            httpDownloader.execute(context.getString(R.string.url)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setAlarmTime(int hour, int minute)
    {
        preferenceManager.writeAlarmTime(hour, minute);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        Log.i("hours", String.format(Locale.getDefault(), "%d", hour));
        Log.i("minutes", String.format(Locale.getDefault(), "%d", minute));

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        Toast.makeText(context, String.format(Locale.getDefault(), context.getString(R.string.alarmSetAt), hour, minute, day_of_month, month, year), Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm()
    {
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
            Toast.makeText(context, context.getString(R.string.alarmCancelled), Toast.LENGTH_SHORT).show();
            preferenceManager.writeAlarmTime(-1, -1);
        }
    }

    public String checkAlarmAtStart()
    {
        int[] values = preferenceManager.getTime();

        if(values[0] == -1 && values[1] == -1)
        {
            return context.getString(R.string.noAlarm);
        }

        else
        {
            return String.format(Locale.getDefault(), context.getString(R.string.alarmSetAt), values[0], values[1], day_of_month, month, year);
        }
    }
}