package com.dalpiazsolutions.coffeealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeReceiver extends BroadcastReceiver {

    MainController mainController;

    @Override
    public void onReceive(Context context, Intent intent) {
        mainController = new MainController(context);
        mainController.getCoffee();
        mainController.lightOn();
    }
}
