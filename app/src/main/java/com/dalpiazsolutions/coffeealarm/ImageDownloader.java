package com.dalpiazsolutions.coffeealarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader extends AsyncTask <String, Void, Bitmap> {

    private URL url;
    private HttpURLConnection urlConnection;
    private InputStream inputStream;
    private Bitmap bitmap;

    @Override
    protected Bitmap doInBackground(String... urls) {

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
