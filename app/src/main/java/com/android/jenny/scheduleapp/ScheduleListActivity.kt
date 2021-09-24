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
import com.android.jenny.scheduleapp.adapter.ScheduleListAdapter
import com.android.jenny.scheduleapp.databinding.ActivityScheduleListBinding
import com.android.jenny.scheduleapp.model.Schedule

class ScheduleListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ScheduleActivity"
        const val ADD_SCHEDULE_KEY = "addScheduleData"
    }

    lateinit var binding: ActivityScheduleListBinding
    private lateinit var scheduleActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var scheduleListAdapter: ScheduleListAdapter

    private val list = mutableListOf<Schedule>()
    var mItemList: MutableList<Schedule> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initRecyclerView()

        scheduleActivityResultLauncher = resultForScheduleAddActivity()
//        getResultData = resultForScheduleAddActivity()
//        initScheduleList()
    }

    private fun resultForScheduleAddActivity(): ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getParcelableExtra<Schedule>(ADD_SCHEDULE_KEY)
                Log.e(TAG, "getAddTimeDetail(): $data")
                if (data != null) {
                    scheduleListAdapter.addItem(data)
//                    mItemList.apply { add(data) }
//                    initScheduleList()
                }
            }
        }

    fun openScheduleAddActivityForResult() {
        val intent = Intent(this@ScheduleListActivity, ScheduleAddActivity::class.java)
        scheduleActivityResultLauncher.launch(intent)
//        getResultData.launch(intent)
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

    private fun initScheduleList() {
        // TODO add: getScheduleListFromServer
        Log.e(TAG, "initScheduleAdapter()")
        scheduleListAdapter = ScheduleListAdapter()
        scheduleListAdapter.data = mItemList

        scheduleListAdapter.setItemClickListener(object: ScheduleListAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val item = mItemList[position]
                Toast.makeText(view.context,
                    "${item.name}\n${item.start_time}-${item.end_time}",
                    Toast.LENGTH_LONG).show()
                scheduleListAdapter.notifyDataSetChanged()
            }
        })

        binding.recyclerviewScheduleList.adapter = scheduleListAdapter
        Log.e(TAG, "init_mItemList: $mItemList")
//        scheduleAdapter.notifyDataSetChanged()

//        list.apply {
//            add(data)
//        }
    }

    private fun initRecyclerView() {
        Log.e(TAG, "initRecyclerView()")
        scheduleListAdapter = ScheduleListAdapter()
        scheduleListAdapter.data = mItemList

        binding.recyclerviewScheduleList.run {
            adapter = scheduleListAdapter
        }
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_list)
        binding.activity = this@ScheduleListActivity
    }
}