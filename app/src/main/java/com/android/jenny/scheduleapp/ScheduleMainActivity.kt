package com.android.jenny.scheduleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.adapter.ScheduleListAdapter
import com.android.jenny.scheduleapp.databinding.ActivityScheduleMainBinding
import com.android.jenny.scheduleapp.model.AllSchedule
import com.android.jenny.scheduleapp.model.Schedule
import org.json.JSONArray
import org.json.JSONObject

class ScheduleMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleMainBinding
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var scheduleAddResultLauncher: ActivityResultLauncher<Intent>

    private val list = mutableListOf<Schedule>()
    var schedules: MutableList<Schedule> = mutableListOf()
    private var allSchedule: AllSchedule? = null

    private var useAll: Boolean = false

    companion object {
        private const val TAG = "ScheduleMainActivity"
        const val ADD_SCHEDULE_DATA = "addScheduleData"
        const val EDIT_SCHEDULE_DATA = "editScheduleData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initRecyclerView()

        scheduleAddResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val key = result.data?.getStringExtra("key")
                val position = result.data?.getIntExtra("position", 1)
                Log.e(TAG, "key: $key")
                Log.e(TAG, "position: $position")
                when (key) {
                    "addScheduleKey" -> {
                        val data = result.data?.getParcelableExtra<Schedule>(ADD_SCHEDULE_DATA)
                        Log.e(TAG, "add_schedule_data: $data")
                        scheduleListAdapter.addItem(data!!)
                    }
                    "editScheduleKey" -> {
                        val data = result.data?.getParcelableExtra<Schedule>(EDIT_SCHEDULE_DATA)
                        Log.e(TAG, "edit_schedule_data: $data")
                        schedules[position!!] = data!!
                        scheduleListAdapter.notifyDataSetChanged()
                    }
                    "removeScheduleKey" -> {
                        Log.e(TAG, "remove_schedule_position: $position")
                        scheduleListAdapter.removeItem(position!!)
                    }
                }
            }
        }

        scheduleListAdapter.setOnEditClickListener(object: ScheduleListAdapter.EditClickListener{
            override fun editClick(position: Int, schedule: Schedule) {
                Log.e(TAG, "editButton Click")
                Log.e(TAG, "editButton_position:$position, editButton_schedule:$schedule")
                openScheduleEditForResult(position,schedule)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // TODO: step1 - load schedule from server
//        useAll = true

        val schedule1 = Schedule(
            "A",
            "n",
            "mon",
            "07:00",
            "20:00"
        )
        val schedule2 = Schedule(
            "B",
            "y",
            "sun,mon,tue,wed,thu,fri,sat",
            "12:00",
            "00:00"
        )
        val schedule3 = Schedule(
            "C",
            "y",
            "mon,fri",
            "08:00",
            "10:00"
        )

        schedules.add(schedule1)
        schedules.add(schedule2)
        schedules.add(schedule3)

        allSchedule = AllSchedule("y", schedules)
        Log.e(TAG, "json_1:$schedules")
        Log.e(TAG, "json_2:$allSchedule")
        scheduleListAdapter.data = allSchedule!!.schedules
//        allSchedule = AllSchedule("1", mItemList)
    }

    fun makeAllScheduleJson() {
//        val scheduleJson = Json.encodeToString(allSchedule)
//        Log.e(TAG, "ScheduleJson: $scheduleJson")
        val schedules = allSchedule!!.schedules
        val jsonScheduleList = JSONArray()
        for (i in schedules.indices) {
            var sObject = JSONObject()
            sObject.put("name", schedules[i].name)
            sObject.put("use", schedules[i].use)
            sObject.put("day", schedules[i].day)
            sObject.put("start", schedules[i].start)
            sObject.put("start", schedules[i].end)
            jsonScheduleList.put(sObject)
        }
        Log.e(TAG, "jsonScheduleList: $jsonScheduleList")

        val allScheduleJsonObject = JSONObject()
        allScheduleJsonObject.put("use_all", allSchedule!!.user_all)
        allScheduleJsonObject.put("schedules", jsonScheduleList)
        Log.e(TAG, "allScheduleJsonObject: $allScheduleJsonObject")

    }

    fun openScheduleEditForResult(position: Int, schedule: Schedule) {
        val intent = Intent(this@ScheduleMainActivity, ScheduleAddEditActivity::class.java)
        intent.putExtra("key","editScheduleKey")
        intent.putExtra("position", position)
        intent.putExtra(EDIT_SCHEDULE_DATA, schedule)
        scheduleAddResultLauncher.launch(intent)
    }

    fun openScheduleAddForResult() {
        val intent = Intent(this@ScheduleMainActivity, ScheduleAddEditActivity::class.java)
        intent.putExtra("key","addScheduleKey")
        scheduleAddResultLauncher.launch(intent)
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

    private fun initRecyclerView() {
        Log.e(TAG, "initRecyclerView()")
//        // TODO add: getScheduleListFromServer
        scheduleListAdapter = ScheduleListAdapter()
//        scheduleListAdapter.data = allSchedule!!.schedules

        binding.recyclerviewScheduleList.run {
            adapter = scheduleListAdapter
        }
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_main)
        binding.activity = this@ScheduleMainActivity
    }
}