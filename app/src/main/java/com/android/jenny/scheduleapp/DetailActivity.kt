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
import com.android.jenny.scheduleapp.databinding.ActivityDetailBinding
import com.android.jenny.scheduleapp.model.Schedule
import java.io.Serializable
import java.util.*

class DetailActivity: AppCompatActivity() {
    private val TAG = "ScheduleDetailActivity"
    private lateinit var binding: ActivityDetailBinding
    private lateinit var textViewTitle: TextView
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.activity = this@DetailActivity

        performDataBinding()
        widgetSetting()
    }

    fun activityForResult() {
        val schedule = getDetail()
        val intent = Intent(this@DetailActivity, ScheduleActivity::class.java).apply {
            putExtra(ScheduleActivity.INTENT_KEY, schedule)
        }
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
        Log.e(TAG, "전달 완료!")
    }

    private fun getDetail(): Schedule {
        return Schedule(
            "1",
            textViewTitle.text.toString(),
            "1100000",
            startTimeButton.text.toString(),
            endTimeButton.text.toString()
        )
    }

    fun editScheduleName() {
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

    fun dayBtnClick(view: View) {
        val state = view.isSelected
        Log.e(TAG, "view.isSelected: $state")
        if (!state) {
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

        var timePicker = TimePickerDialog(
            this,
            2,
            { _, sHour, sMinute ->
                updateTime(view, sHour, sMinute)
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

    private fun widgetSetting() {
        textViewTitle = binding.textviewTitle
        startTimeButton = binding.buttonStartTime
        endTimeButton = binding.buttonEndTime
    }

    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.activity = this@DetailActivity
    }
}