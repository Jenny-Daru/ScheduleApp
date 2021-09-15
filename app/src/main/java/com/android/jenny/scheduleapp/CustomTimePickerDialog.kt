package com.android.jenny.scheduleapp

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.widget.NumberPicker
import android.widget.TimePicker
import java.lang.reflect.Field


class CustomTimePickerDialog: TimePickerDialog.OnTimeSetListener {
    companion object {
        private val TAG = "CustomTimePickerDialog"
        private const val TIME_PICKER_INTERVAL = 10
    }

    private lateinit var timePicker: TimePicker
    private lateinit var mTimeSetListener: TimePickerDialog.OnTimeSetListener

    override fun onTimeSet(view: TimePicker?, tHour: Int, tMinute: Int) {

    }


}