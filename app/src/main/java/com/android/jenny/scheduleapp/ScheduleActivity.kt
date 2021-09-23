package com.android.jenny.scheduleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.adapter.ScheduleRecyclerAdapter
import com.android.jenny.scheduleapp.databinding.ActivityScheduleBinding
import com.android.jenny.scheduleapp.model.Schedule

class ScheduleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ScheduleActivity"
         const val INTENT_KEY = "SCHEDULE"
    }

    lateinit var binding: ActivityScheduleBinding
    private lateinit var getResultData: ActivityResultLauncher<Intent>
    private lateinit var scheduleAdapter: ScheduleRecyclerAdapter

    private val list = mutableListOf<Schedule>()

    var mItemList: MutableList<Schedule> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        getResultData = resultForDetailActivity()
//        initScheduleList()
    }

    private fun initScheduleList() {
        // TODO add: getScheduleListFromServer
        Log.e(TAG, "initScheduleAdapter()")
        scheduleAdapter = ScheduleRecyclerAdapter()
        scheduleAdapter.data = mItemList

        scheduleAdapter.setItemClickListener(object: ScheduleRecyclerAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
               val item = mItemList[position]
               Toast.makeText(view.context,
               "${item.name}\n${item.start_time}-${item.end_time}",
               Toast.LENGTH_LONG).show()
               scheduleAdapter.notifyDataSetChanged()
            }
        })

        binding.recyclerviewSchedule.adapter = scheduleAdapter
        Log.e(TAG, "init_mItemList: $mItemList")
//        scheduleAdapter.notifyDataSetChanged()

//        list.apply {
//            add(data)
//        }
    }

    fun moveDetailActivity() {
        var intent = Intent(this@ScheduleActivity, DetailActivity::class.java)
        getResultData.launch(intent)
    }

    private fun resultForDetailActivity(): ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getParcelableExtra<Schedule>(INTENT_KEY)
//                val data = result.data?.getSerializableExtra(INTENT_KEY)
                Log.e(TAG, "getAddTimeDetail(): $data")
                if (data != null) {
                    mItemList.apply { add(data) }
                    initScheduleList()
                }
            }
        }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule)
        binding.activity = this@ScheduleActivity
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