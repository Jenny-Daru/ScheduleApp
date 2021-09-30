package com.android.jenny.scheduleapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.databinding.ActivityScheduleAddEditBinding
import com.android.jenny.scheduleapp.model.Schedule
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAddEditActivity: AppCompatActivity() {
    companion object {
        private const val TAG = "ScheduleAddEditActivity"
    }
    private lateinit var binding: ActivityScheduleAddEditBinding
    private lateinit var textViewName: TextView
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    private lateinit var sunButton: Button
    private lateinit var monButton: Button
    private lateinit var tuesButton: Button
    private lateinit var wedButton: Button
    private lateinit var thursButton: Button
    private lateinit var friButton: Button
    private lateinit var satButton: Button

    private var flag: Boolean = false // Add: false, Edit: true
    private lateinit var schedule: Schedule
    private lateinit var key: String
    private var position: Int? = null

    private var dayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate - start")
        super.onCreate(savedInstanceState)

        key = getIntentKey()
        performDataBinding()
        when (key) {
            "editScheduleKey" -> {
                position = intent.getIntExtra("position", 1)
                schedule = intent.getParcelableExtra(ScheduleMainActivity.EDIT_SCHEDULE_DATA)!!
                binding.buttonRemove.visibility = View.VISIBLE
                initEditScheduleData(schedule)
            }
        }
    }

    private fun getIntentKey(): String {
        Log.e(TAG, "getIntentKey() -start")
        return intent.getStringExtra("key").toString()
    }

    fun removeBtnClick() {
        Log.e(TAG, "removeBtnClick - start")
        removeForScheduleListActivity()
    }

    fun saveForScheduleListActivity() {
        val intent = Intent(this@ScheduleAddEditActivity, ScheduleMainActivity::class.java)
        when(key) {
            "editScheduleKey" -> {
                intent.putExtra("key", "editScheduleKey")
                intent.putExtra("position", position)
                intent.putExtra(ScheduleMainActivity.EDIT_SCHEDULE_DATA, getInputData())
            }
            else -> {
                intent.putExtra("key", "addScheduleKey")
                intent.putExtra(ScheduleMainActivity.ADD_SCHEDULE_DATA, getInputData())
            }
        }
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
        Log.e(TAG, "$key _data 전달 완료!")
    }

    private fun removeForScheduleListActivity() {
        Log.e(TAG, "removeForScheduleListActivity - start")
        val intent = Intent(this@ScheduleAddEditActivity, ScheduleMainActivity::class.java)
        intent.putExtra("key", "removeScheduleKey")
        intent.putExtra("position", position)
//        intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, getInputData())
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
    }

    private fun getInputData(): Schedule {
        return Schedule(
            "1",
            textViewName.text.toString(),
            dayList.distinct().joinToString(","),
            startTimeButton.text.toString(),
            endTimeButton.text.toString()
        )
    }

    fun settingScheduleNameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit_schedule_name)

        val input = EditText(this)
        input.hint = textViewName.text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Ok") { _, _ ->
            val editName = input.text.toString()
            textViewName.text = editName
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    fun switchBtnClick(view: View) {
        Log.e(TAG, "switch.isSelected: ${view.isSelected}")
        val state = !view.isSelected
        if (state) {
            view.isSelected = true
            Log.e(TAG, "switch is on")
        } else {
            view.isSelected = false
            Log.e(TAG, "switch is off")
        }
    }

    private fun getDayButtonBeforeEdit(days: String) {
        // ,구분
        // id 반환
        var view = currentFocus
        var button: Button? = null
        val day_arr = days.split(",")
        for (x in day_arr.indices) {
            when (day_arr[x]) {
                resources.getString(R.string.sun_day) -> {
                    sunButton.setBackgroundResource(R.drawable.shape_circle_selected)
                    dayList.add("Sun")
                }
                resources.getString(R.string.mon_day) -> monButton.setBackgroundResource(R.drawable.shape_circle_selected)
                resources.getString(R.string.tues_day) -> tuesButton.setBackgroundResource(R.drawable.shape_circle_selected)
                resources.getString(R.string.wed_day) -> wedButton.setBackgroundResource(R.drawable.shape_circle_selected)
                resources.getString(R.string.thurs_day) -> thursButton.setBackgroundResource(R.drawable.shape_circle_selected)
                resources.getString(R.string.fri_day) -> friButton.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            Log.e(TAG, "day_arr_for: $day_arr[$x]: ${day_arr[x]}")
        }
    }

    fun changeButtonBackground(button: Button) {
        button.setBackgroundResource(R.drawable.shape_circle_selected)
    }

    private fun getValueFromDayButton(id: Int): String? {
        var day: String? = null
        when (id) {
            sunButton.id -> day = resources.getString(R.string.sun_day)
            monButton.id -> day = "Mon"
            tuesButton.id -> day = "Tue"
            wedButton.id -> day = "Wed"
            thursButton.id -> day = "Thu"
            friButton.id -> day = "Fri"
            satButton.id -> day = "Sat"
        }
        Log.e(TAG, "day: $day")
        return day
    }

    fun dayBtnClick(view: View) {
        val day = getValueFromDayButton(view.id)

        view.isSelected = !view.isSelected
        Log.e(TAG, "dayBtn_isSelected: ${view.isSelected}")
        if (view.isSelected) {
            view.textDirection
            view.setBackgroundResource(R.drawable.shape_circle_selected)
            dayList.add(day!!)
        } else {
            view.setBackgroundResource(R.drawable.shape_circle)
            dayList.remove(day)
        }
        Log.e(TAG, "dayList: $dayList")
    }

    fun settingTime(view: View) {
        val cal: Calendar = Calendar.getInstance()
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cal.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            2,
            { _, sHour, sMinute ->
                setTime(view, sHour, sMinute) },
            hour,
            minutes,
            true
        )
        timePicker.show()
    }

    private fun setTime(view: View, sHour: Int, sMinute: Int) {
        when (view.id) {
            R.id.button_startTime -> startTimeButton.text = String.format("%02d:%02d", sHour, sMinute)
            R.id.button_endTime -> endTimeButton.text = String.format("%02d:%02d", sHour, sMinute)
        }
    }

    private fun initEditScheduleData(data: Schedule) {
        Log.e(TAG, "initEditScheduleData - start")
        Log.e(TAG, "initEditData: $data")
        textViewName.text = data.name
        startTimeButton.text = data.start_time
        endTimeButton.text = data.end_time
        getDayButtonBeforeEdit(data.day)
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding - start")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_add_edit)
        binding.activity = this@ScheduleAddEditActivity

        textViewName = binding.textviewName
        startTimeButton = binding.buttonStartTime
        endTimeButton = binding.buttonEndTime

        sunButton = binding.buttonSun
        monButton = binding.buttonMon
        tuesButton = binding.buttonTues
        wedButton = binding.buttonWed
        thursButton = binding.buttonThurs
        friButton = binding.buttonFri
        satButton = binding.buttonSat
    }


}