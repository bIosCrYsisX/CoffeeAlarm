package com.dalpiazsolutions.coffeealarm;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HTTPDownloader extends AsyncTask<String, Void, String> {

    private URL url;
    private HttpURLConnection urlConnection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuilder result = new StringBuilder();
    private String lines = new String();

    @Override
    protected String doInBackground(String... urls) {
        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while((lines = bufferedReader.readLine()) != null)
            {
                result.append(lines);
            }
            Log.i("result", result.toString());
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

