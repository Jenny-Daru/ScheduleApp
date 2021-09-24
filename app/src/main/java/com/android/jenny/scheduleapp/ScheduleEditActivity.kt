package com.android.jenny.scheduleapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.databinding.ActivityScheduleEditBinding
import com.android.jenny.scheduleapp.model.Schedule

class ScheduleEditActivity: AppCompatActivity() {

    companion object {
        private const val TAG = "ScheduleEditActivity"
    }
    private lateinit var binding: ActivityScheduleEditBinding
    private lateinit var textViewName: TextView
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    private lateinit var schedule: Schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule = getData()

        performDataBinding()
        initData(schedule)
    }

    fun removeForScheduleListActivity() {

    }

    private fun getData(): Schedule {
        val intent = intent
        return intent.getParcelableExtra("modifySchedule")!!
    }

    private fun initData(data: Schedule) {
        textViewName.text = data.name
        startTimeButton.text = data.start_time
        endTimeButton.text = data.end_time
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_edit)
        binding.activity = this@ScheduleEditActivity

        textViewName = binding.textviewName
        startTimeButton = binding.buttonStartTime
        endTimeButton = binding.buttonEndTime
    }



//    fun onClose(view: View) {
//        val intent = Intent()
//        intent.putExtra("modifyResult", )
//        setResult(RESULT_OK, intent)
//
//        finish()
//    }



}