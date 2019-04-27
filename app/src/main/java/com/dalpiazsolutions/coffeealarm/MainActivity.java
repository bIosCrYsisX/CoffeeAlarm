package com.dalpiazsolutions.coffeealarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setAlarmButton;
    private Button getCoffeeButton;
    private Button cancelAlarmButton;
    private TextView txtAlarmInfo;
    private MainController mainController;
    int hourAlarm;
    int minuteAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = new MainController(this);

        timePicker = findViewById(R.id.alarmTimePicker);
        setAlarmButton = findViewById(R.id.btnSetAlarm);
        getCoffeeButton = findViewById(R.id.btnCoffee);
        cancelAlarmButton = findViewById(R.id.btnCancelAlarm);
        txtAlarmInfo = findViewById(R.id.txtAlarm);

        txtAlarmInfo.setText(mainController.checkAlarmAtStart());

        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hourAlarm = hourOfDay;
                minuteAlarm = minute;
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.setAlarmTime(hourAlarm, minuteAlarm);
            }
        });

        getCoffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.getCoffee();
            }
        });

        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.cancelAlarm();
            }
        });
    }
}
