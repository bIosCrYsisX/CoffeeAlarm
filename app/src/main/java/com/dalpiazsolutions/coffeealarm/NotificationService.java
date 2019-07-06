package com.dalpiazsolutions.coffeealarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationService extends Service {

    private Notifier notifier;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifier = new Notifier(this);
        notifier.throwNotification(intent.getStringExtra("title"), intent.getStringExtra("text"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
