package com.dalpiazsolutions.coffeealarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setAlarmButton;
    private Button getCoffeeButton;
    private Button cancelAlarmButton;
    private Button lightOnButton;
    private Button lightOffButton;
    private TextView txtAlarmInfo;
    private MainController mainController;
    private Switch intervalSwitch;
    private int hourAlarm;
    private int minuteAlarm;
    private boolean interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = new MainController(this);

        timePicker = findViewById(R.id.alarmTimePicker);
        setAlarmButton = findViewById(R.id.btnSetAlarm);
        getCoffeeButton = findViewById(R.id.btnCoffee);
        cancelAlarmButton = findViewById(R.id.btnCancelAlarm);
        lightOnButton = findViewById(R.id.btnLightOn);
        lightOffButton= findViewById(R.id.btnLightOff);
        intervalSwitch = findViewById(R.id.swInterval);
        txtAlarmInfo = findViewById(R.id.txtAlarm);

        txtAlarmInfo.setText(mainController.checkAlarmAtStart());

        timePicker.setIs24HourView(true);

        intervalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                interval = isChecked;
            }
        });

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
                mainController.setAlarmTime(hourAlarm, minuteAlarm, interval);
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

        lightOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.lightOn();
            }
        });

        lightOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.lightOff();
            }
        });
    }

    public void update()
    {
        txtAlarmInfo.setText(mainController.checkAlarmAtStart());
    }
}
