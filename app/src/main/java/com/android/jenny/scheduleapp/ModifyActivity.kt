package com.android.jenny.scheduleapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.databinding.ActivityModifyBinding
import com.android.jenny.scheduleapp.model.Schedule

class ModifyActivity: AppCompatActivity() {

    companion object {
        private const val TAG = "ModifyActivity"
    }
    private lateinit var binding: ActivityModifyBinding
    private lateinit var textViewTitle: TextView
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    private lateinit var schedule: Schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.activity_detail)

        performDataBinding()
    }

    private fun getData(): Schedule {
        var intent = intent
        return intent.getParcelableExtra("modifySchedule")!!
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify)
        binding.activity = this@ModifyActivity

        upLoad(getData())
    }

    private fun upLoad(data: Schedule) {
        binding.textviewTitle.text = data.name
        binding.buttonStartTime.text = data.start_time
        binding.buttonEndTime.text = data.end_time
    }


//    fun onClose(view: View) {
//        val intent = Intent()
//        intent.putExtra("modifyResult", )
//        setResult(RESULT_OK, intent)
//
//        finish()
//    }



}