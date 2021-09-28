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
import com.android.jenny.scheduleapp.databinding.ActivityScheduleAddBinding
import com.android.jenny.scheduleapp.model.Schedule
import java.util.*

class ScheduleAddActivity: AppCompatActivity() {
    companion object {
        private const val TAG = "ScheduleAddActivity"
    }

    private lateinit var binding: ActivityScheduleAddBinding
    private lateinit var textViewName: TextView
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    private var flag: Boolean = false // Add: false, Edit: true
    private lateinit var schedule: Schedule
    private lateinit var key: String
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate - start")

        key = getIntentKey()

        performDataBinding()
        when (key) {
            "editScheduleKey" -> {
                position = intent.getStringExtra("position")?.toInt()
                schedule = intent.getParcelableExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA)!!
                binding.buttonRemove.visibility = View.VISIBLE
                initEditScheduleData(schedule)
            }
        }

//        schedule = getIntentKeyInfo()
//        getIntentKeyInfo()
//        initEditScheduleData(schedule)
    }

    private fun getIntentKey(): String {
        return intent.getStringExtra("key").toString()
    }

    fun removeBtnClick() {
        Log.e(TAG, "removeBtnClick - start")
        removeForScheduleListActivity()
    }

    fun saveForScheduleListActivity() {
        val intent = Intent(this@ScheduleAddActivity, ScheduleListActivity::class.java)
        when(key) {
            "editScheduleKey" -> {
                intent.putExtra("key", "editScheduleKey")
                intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, getInputData())
            }
            else -> {
                intent.putExtra("key", "addScheduleKey")
                intent.putExtra(ScheduleListActivity.ADD_SCHEDULE_DATA, getInputData())
            }
        }
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
        Log.e(TAG, "$key _data 전달 완료!")
    }

    private fun removeForScheduleListActivity() {
        Log.e(TAG, "removeForScheduleListActivity - start")
        val intent = Intent(this@ScheduleAddActivity, ScheduleListActivity::class.java)
        intent.putExtra("key", "removeScheduleKey")
        intent.putExtra("position", position)
        intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, getInputData())
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause()")
        this.position = null
    }

    private fun getInputData(): Schedule {
        return Schedule(
            "1",
            textViewName.text.toString(),
            "1100000",
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
    }

    private fun getIntentKeyInfo(): Schedule {
        Log.e(TAG, "getIntentKeyInfo - start")
        val intent = intent
        lateinit var data: Schedule
        key = intent.getStringExtra("key").toString()
        Log.e(TAG, "key: $key")
        if (key == "editScheduleKey") {
            data = intent.getParcelableExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA)!!
            binding.buttonRemove.visibility = View.VISIBLE
            Log.e(TAG, "getIntentKeyInfo_edit_data - ${data!!}")
//            initEditScheduleData(data)
        }
        return data
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding - start")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_add)
        binding.activity = this@ScheduleAddActivity

        textViewName = binding.textviewName
        startTimeButton = binding.buttonStartTime
        endTimeButton = binding.buttonEndTime
    }

}