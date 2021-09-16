package com.android.jenny.scheduleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private val TAG = "ScheduleActivity"
    lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule)
        binding.activity = this@ScheduleActivity
    }

    fun nextDetailActivity() {
        var intent = Intent(this@ScheduleActivity, DetailActivity::class.java)
        startActivity(intent)
    }

    fun switchBtnClick(view: View) {
        Log.e(TAG, "Main.switch.isSelected: ${view.isSelected}")
        val state = !view.isSelected
        if (state) {
            view.isSelected = true
            Log.e(TAG, "switch is on")
        } else {
            view.isSelected = false
            Log.e(TAG, "switch is off")
        }
    }
}