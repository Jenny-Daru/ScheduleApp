package com.android.jenny.scheduleapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTimePickerDialog extends TimePickerDialog {
    private static final String TAG = "CustomTimePickerDialog";

    private final static int TIME_PICKER_INTERVAL = 10;
    private final static int TIME_PICKER_THEME = 3;
    private TimePicker timePicker;
    private final OnTimeSetListener listener;

    private int lastHour = -1;
    private int lastMinute = -1;


    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TIME_PICKER_THEME, null, hourOfDay, minute / TIME_PICKER_INTERVAL, is24HourView);
        lastHour = hourOfDay;
        lastMinute = minute;
        this.listener = listener;
    }


    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        timePicker.setHour(hourOfDay);
        timePicker.setMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (listener != null) {
                    timePicker.clearFocus();
                    listener.onTimeSet(timePicker, timePicker.getHour(),
                            timePicker.getMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            timePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "msg:" + e.getMessage());
        }
    }


    // onTimeChanged()

//    @Override
//    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//        super.onTimeChanged(view, hourOfDay, minute);
//        if (lastHour != hourOfDay && lastMinute != minute) {
//            view.setHour(lastHour);
//        } else {
//            lastHour = hourOfDay;
//        }
//        lastMinute = minute;
//    }
}
