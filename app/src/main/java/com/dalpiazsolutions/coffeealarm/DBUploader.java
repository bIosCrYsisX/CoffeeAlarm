package com.dalpiazsolutions.coffeealarm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class DBUploader extends AsyncTask<String, Integer, Boolean> {

    private String address;
    private String user;
    private String password;
    private Context context;
    private PreferenceManager preferenceManager;
    private MainController mainController;


    public DBUploader(Context context)
    {
        this.context = context;
        address = context.getString(R.string.ip);
        user = context.getString(R.string.user);
        password = context.getString(R.string.password);
        mainController = new MainController(context);
        preferenceManager = new PreferenceManager(context);
        preferenceManager.start();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        File file = new File(context.getString(R.string.DB_PATH));
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(address);
            ftpClient.login(user, password);
            ftpClient.changeWorkingDirectory(context.getString(R.string.directory));
            ftpClient.setFileTransferMode(ftpClient.BINARY_FILE_TYPE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            FileInputStream is = new FileInputStream(file);
            BufferedInputStream buffIn = new BufferedInputStream(is);
            ftpClient.enterLocalPassiveMode();
            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis();
            ftpClient.storeFile(String.format(Locale.getDefault(), context.getString(R.string.fileName), time), buffIn);
            Log.i("file", String.format(Locale.getDefault(), context.getString(R.string.fileName), time));
            buffIn.close();

            file = new File(context.getString(R.string.DB_PATH_SHM));
            is = new FileInputStream(file);
            buffIn = new BufferedInputStream(is);
            ftpClient.storeFile(String.format(Locale.getDefault(), context.getString(R.string.fileNameSHM), time), buffIn);
            Log.i("file", String.format(Locale.getDefault(), context.getString(R.string.fileNameSHM), time));
            buffIn.close();

            file = new File(context.getString(R.string.DB_PATH_WAL));
            is = new FileInputStream(file);
            buffIn = new BufferedInputStream(is);
            ftpClient.storeFile(String.format(Locale.getDefault(), context.getString(R.string.fileNameWAL), time), buffIn);
            Log.i("file", String.format(Locale.getDefault(), context.getString(R.string.fileNameWAL), time));
            buffIn.close();

            ftpClient.logout();
            ftpClient.disconnect();

            return true;
        } catch (IOException e) {
            Log.i("Exception", "EXCEPTION");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if(success)
        {
            Toast.makeText(context, context.getString(R.string.finished), Toast.LENGTH_SHORT).show();
            mainController.nukeTable();
            preferenceManager.setDayUploaded();
        }
        else
        {
            Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }
    }
}
