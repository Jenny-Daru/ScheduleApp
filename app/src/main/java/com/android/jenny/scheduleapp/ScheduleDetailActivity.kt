package com.android.jenny.scheduleapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ScheduleDetailActivity: AppCompatActivity() {

    private val TAG = "ScheduleDetailActivity"
    
    private lateinit var textViewTitle: TextView
    
    private lateinit var editNameButton: Button
    
    private lateinit var sunButton: Button
    private lateinit var monButton: Button
    private lateinit var tuesButton: Button
    private lateinit var wedButton: Button
    private lateinit var thursButton: Button
    private lateinit var friButton: Button
    private lateinit var satButton: Button
    
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)
        
        textViewTitle = findViewById(R.id.textview_title)
        editNameButton = findViewById(R.id.button_editTitle)
        sunButton = findViewById(R.id.button_sun)
        monButton = findViewById(R.id.button_mon)
        tuesButton = findViewById(R.id.button_tues)
        wedButton = findViewById(R.id.button_wed)
        thursButton = findViewById(R.id.button_thurs)
        friButton = findViewById(R.id.button_fri)
        satButton = findViewById(R.id.button_sat)

        startTimeButton = findViewById(R.id.button_startTime)
        endTimeButton = findViewById(R.id.button_endTime)
    }
    
    fun editScheduleName(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit_schedule_name)
        
        val input = EditText(this)
        input.hint = textViewTitle.text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        
        builder.setPositiveButton("Ok") { _, _ ->
            var editName = input.text.toString()
            textViewTitle.text = editName
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    fun dayBtnClick(view: View) {
        var state = view.isSelected
        Log.e(TAG, "view.isSelected: $state")
        if (!view.isSelected) {
            view.isSelected = true
            view.setBackgroundResource(R.drawable.shape_circle_selected)
        } else {
            view.isSelected = false
            view.setBackgroundResource(R.drawable.shape_circle)
        }
    }

    fun timeSetting(view: View) {
        val cal: Calendar = Calendar.getInstance()
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cal.get(Calendar.MINUTE)

        // time picker dialog
        var timePicker = TimePickerDialog(
            this,
            2,
            { _, sHour, sMinute ->
                updateTime(view, sHour, sMinute)
//                startTimeButton.text = String.format("%02d:%02d", sHour, sMinute)
            },
            hour,
            minutes,
            true
        )
        timePicker.show()
    }

    private fun updateTime(view: View, sHour: Int, sMinute: Int) {
        when (view.id) {
            R.id.button_startTime -> startTimeButton.text = String.format("%02d:%02d", sHour, sMinute)
            R.id.button_endTime -> endTimeButton.text = String.format("%02d:%02d", sHour, sMinute)
        }
    }
}