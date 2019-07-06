package com.dalpiazsolutions.coffeealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.room.Room;

import com.dalpiazsolutions.coffeealarm.dao.ItemDAO;
import com.dalpiazsolutions.coffeealarm.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {

    private PreferenceManager preferenceManager;
    private HTTPDownloader httpDownloader;
    private Context context;
    private MainActivity mainActivity;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private PriceDB priceDB;
    private int day_of_month;
    private int month;
    private int year;
    private boolean state = false;

    public MainController(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        this.context = mainActivity;
        preferenceManager = new PreferenceManager(context);
        preferenceManager.start();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        priceDB = Room.databaseBuilder(mainActivity.getApplicationContext(), PriceDB.class, "actionDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public MainController(Context context)
    {
        preferenceManager = new PreferenceManager(context);
        preferenceManager.start();
        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        priceDB = Room.databaseBuilder(context.getApplicationContext(), PriceDB.class, "actionDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public void getCoffee()
    {
        getSite(context.getString(R.string.urlCoffee));
    }

    public void lightOn()
    {
        getSite(context.getString(R.string.urlLightOn));
        Toast.makeText(context, context.getString(R.string.lightON), Toast.LENGTH_SHORT).show();
    }

    public void lightOff()
    {
        getSite(context.getString(R.string.urlLightOff));
        Toast.makeText(context, context.getString(R.string.lightOff), Toast.LENGTH_SHORT).show();
    }

    public void setAlarmTime(int hour, int minute, boolean interval)
    {
        preferenceManager.writeAlarmTime(hour, minute);
        preferenceManager.writeInterval(interval);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if((hour == calendar.get(Calendar.HOUR_OF_DAY) && minute <= (calendar.get(Calendar.MINUTE))) || (hour < calendar.get(Calendar.HOUR_OF_DAY)))
        {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        Log.i("hours", String.format(Locale.getDefault(), "%d", hour));
        Log.i("minutes", String.format(Locale.getDefault(), "%d", minute));

        if(!interval)
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
        else
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
        Toast.makeText(context, String.format(Locale.getDefault(), context.getString(R.string.alarmSetAtDate), hour, minute, day_of_month, month, year), Toast.LENGTH_LONG).show();
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
            if(preferenceManager.getInterval())
            {
                return String.format(Locale.getDefault(), context.getString(R.string.alarmSetAt24), values[0], values[1]);
            }
            else
            {
                return String.format(Locale.getDefault(), context.getString(R.string.alarmSetAt), values[0], values[1]);
            }
        }
    }

    public float[] getInsideWeather()
    {
        String site = getSite(context.getString(R.string.urlSite));
        float[] values = new float[3];
        values[0] = Float.parseFloat(site.substring(146, 151));
        values[1] = Float.parseFloat(site.substring(155, 160));
        values[2] = Float.parseFloat(site.substring(172, 176));
        return values;
    }

    public double[] getOutsideWeather()
    {
        String site = getSite(String.format(Locale.getDefault(), context.getString(R.string.jsonURL), context.getString(R.string.appid), context.getString(R.string.linz)));
        double[] values = new double[2];
        try {
            JSONObject jsonObject = new JSONObject(site);
            String partString = jsonObject.getString("main");
            jsonObject = new JSONObject(partString);
            for(int i = 0; i < jsonObject.length(); i++)
            {
                values[0] = jsonObject.getDouble("temp") - 273.15;
                values[1] = jsonObject.getDouble("humidity");
            }

            return values;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getIcon()
    {
        try {
            String iconID = "";
            String site = getSite(String.format(Locale.getDefault(), context.getString(R.string.jsonURL), context.getString(R.string.appid), context.getString(R.string.linz)));
            JSONObject jsonObject = new JSONObject(site);
            String partString = jsonObject.getString("weather");
            JSONArray jsonArray = new JSONArray(partString);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                iconID = jsonObject.getString("icon");
            }

            ImageDownloader iconDownloader = new ImageDownloader();
            Log.i("bitmap", String.format(Locale.getDefault(), context.getString(R.string.iconURL), iconID));
            return iconDownloader.execute(String.format(Locale.getDefault(), context.getString(R.string.iconURL), iconID)).get();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSite(String url)
    {
        httpDownloader = new HTTPDownloader();
        try {
            return httpDownloader.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayAdapter<String> getActionValues()
    {
        LinkedList<String> listStrings = new LinkedList<>();
        httpDownloader = new HTTPDownloader();
        try {
            String site = httpDownloader.execute(context.getString(R.string.urlAction)).get();
            Pattern pattern = Pattern.compile("\"price\":(.*?),\"oldPrice\"");
            Matcher matcher = pattern.matcher(site);

            LinkedList<Float> prices = new LinkedList<>();

            int i = 0;
            while (matcher.find())
            {
                prices.add(Float.parseFloat(matcher.group(1)));
                Log.i("prices", prices.get(i).toString());
                i++;
            }
            Log.i("length", Integer.toString(prices.size()));

            pattern = Pattern.compile("retailer\":\\{\"id\":\\d\\d\\d\\d\\d,\"name\":\"(.*?)\",\"uniqueName\"");
            matcher = pattern.matcher(site);

            LinkedList<String> shops = new LinkedList<>();

            i = 0;
            while (matcher.find())
            {
                shops.add(matcher.group(1));
                Log.i("shops", shops.get(i));
                i++;
            }
            Log.i("length", Integer.toString(shops.size()));

            pattern = Pattern.compile("validFrom\":\"(.*?)\"");
            matcher = pattern.matcher(site);

            LinkedList<String> starts = new LinkedList<>();

            i = 0;
            while (matcher.find())
            {
                starts.add(matcher.group(1));
                Log.i("starts", starts.get(i));
                i++;
            }
            Log.i("length", Integer.toString(starts.size()));

            pattern = Pattern.compile("validTo\":\"(.*?)\"");
            matcher = pattern.matcher(site);

            LinkedList<String> ends = new LinkedList<>();

            i = 0;
            while (matcher.find())
            {
                ends.add(matcher.group(1));
                Log.i("starts", ends.get(i));
                i++;
            }
            Log.i("length", Integer.toString(ends.size()));

            if(!preferenceManager.getAlreadyChecked())
            {
                for(i = 0; i < prices.size(); i++)
                {
                    listStrings.add(String.format(Locale.getDefault(), context.getString(R.string.listString), prices.get(i), shops.get(i), starts.get(i), ends.get(i)));
                    insertItem(prices.get(i), shops.get(i), starts.get(i), ends.get(i));
                }
                preferenceManager.setDayChecked();
            }

            else
            {
                for(i = 0; i < prices.size(); i++)
                {
                    listStrings.add(String.format(Locale.getDefault(), context.getString(R.string.listString), prices.get(i), shops.get(i), starts.get(i), ends.get(i)));
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayAdapter(context, android.R.layout.simple_list_item_1, listStrings);
    }

    public void insertItem(float price, String shop, String start, String end)
    {
        ItemDAO itemDAO = priceDB.getItemDAO();
        Item item = new Item();
        item.setPrice(price);
        item.setShop(shop);
        item.setStart(start);
        item.setEnd(end);
        itemDAO.insert(item);
        Log.i("inserted", "inserted");
    }

    public void uploadDB()
    {
        if(preferenceManager.getAlreadyChecked())
        {
            Toast.makeText(context, context.getString(R.string.alreadyUploaded), Toast.LENGTH_SHORT).show();
        }

        else {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiMgr.isWifiEnabled()) {
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

                if (wifiInfo.getNetworkId() != -1) {
                    String ssid = info.getExtraInfo().substring(1, info.getExtraInfo().length() - 1);
                    Log.i("SSID", ssid);
                    Log.i("SSID", context.getString(R.string.SSID));

                    if (ssid.equals(context.getString(R.string.SSID))) {
                        Log.i("equal", "equal");

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Thread", "started");
                                DBUploader dbUploader = new DBUploader(context);
                                setState(dbUploader.uploadDB());
                            }
                        });
                        thread.start();

                        try {
                            thread.join();

                            if (isState()) {
                                Toast.makeText(context, context.getString(R.string.finished), Toast.LENGTH_SHORT).show();
                                setState(false);
                                nukeTable();
                            } else {
                                Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void nukeTable()
    {
        ItemDAO itemDAO = priceDB.getItemDAO();
        itemDAO.nukeTable();
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void getPrices()
    {
        ItemDAO itemDAO = priceDB.getItemDAO();
        Log.i("numberOfValues", Integer.toString(itemDAO.getItems().size()));
    }
}
