package com.android.jenny.scheduleapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.adapter.ScheduleListAdapter
import com.android.jenny.scheduleapp.databinding.ActivityScheduleMainBinding
import com.android.jenny.scheduleapp.model.AllSchedule
import com.android.jenny.scheduleapp.model.Schedule
import org.json.JSONArray
import org.json.JSONObject

class ScheduleMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleMainBinding
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var scheduleAddEditResultLauncher: ActivityResultLauncher<Intent>
//    private lateinit var useAllSwitch: SwitchCompat
    private lateinit var useAllButton: ImageButton
    private lateinit var scheduleListRecyclerView: RecyclerView
    private var useAllOnOff: Boolean = false

    private val list = mutableListOf<Schedule>()
    var schedules: MutableList<Schedule> = mutableListOf()
    private var allSchedule: AllSchedule? = null
    var useAll = "n"
    //

    companion object {
        private const val TAG = "ScheduleMainActivity"
        const val ADD_SCHEDULE_DATA = "addScheduleData"
        const val EDIT_SCHEDULE_DATA = "editScheduleData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
        initRecyclerView()
        Log.e(TAG, "onCreate()_useAllSate:${useAllButton.isSelected}")

        scheduleAddEditResultLauncher = registerForActivityResult(
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

        scheduleListAdapter.setScheduleItemClickListener(object: ScheduleListAdapter.OnScheduleItemClickListener {
            override fun onScheduleItemClick(view: View, position: Int, schedule: Schedule) {
                openScheduleEditForResult(position,schedule)
            }
        })

//        scheduleListAdapter.setOnEditClickListener(object: ScheduleListAdapter.EditClickListener{
//            override fun editClick(position: Int, schedule: Schedule) {
//                Log.e(TAG, "editButton Click")
//                Log.e(TAG, "editButton_position:$position, editButton_schedule:$schedule")
//                openScheduleEditForResult(position,schedule)
//            }
//        })
    }

    override fun onResume() {
        super.onResume()
        // TODO: step1 - load schedule from server
        val schedule1 = Schedule(
            "A",
            "n",
            "Mon",
            "07:00",
            "20:00"
        )
        val schedule2 = Schedule(
            "B",
            "y",
            "Sun,Tue,Wed,Fri,Sat",
            "12:00",
            "00:00"
        )
        val schedule3 = Schedule(
            "C",
            "y",
            "Mon,Fri",
            "08:00",
            "10:00"
        )
        schedules.add(schedule1)
        schedules.add(schedule2)
        schedules.add(schedule3)

        allSchedule = AllSchedule(useAll, schedules)
        scheduleListAdapter.data = allSchedule!!.schedules

        Log.e(TAG, "json_1:$schedules")
        Log.e(TAG, "json_2:$allSchedule")
        Log.e(TAG, "onResume()_useAllState: ${allSchedule!!.use_all}")
        setUseAllSwitchState(checkUseAllSwitchState(allSchedule!!.use_all))
    }

    fun makeAllScheduleJson() {
        val schedules = allSchedule!!.schedules
        val useAll = allSchedule!!.use_all
        val jsonScheduleList = JSONArray()
        for (i in schedules.indices) {
            val sObject = JSONObject()
            sObject.put("name", schedules[i].name)
            sObject.put("use", schedules[i].use)
            sObject.put("day", schedules[i].day)
            sObject.put("start", schedules[i].start)
            sObject.put("start", schedules[i].end)
            jsonScheduleList.put(sObject)
        }
        Log.e(TAG, "jsonScheduleList: $jsonScheduleList")

        val allScheduleJsonObject = JSONObject()
        allScheduleJsonObject.put("use_all", useAll)
        allScheduleJsonObject.put("schedules", jsonScheduleList)
        Log.e(TAG, "allScheduleJsonObject: $allScheduleJsonObject")
    }

    fun openScheduleEditForResult(position: Int, schedule: Schedule) {
        val intent = Intent(this@ScheduleMainActivity, ScheduleAddEditActivity::class.java)
        intent.putExtra("key","editScheduleKey")
        intent.putExtra("position", position)
        intent.putExtra(EDIT_SCHEDULE_DATA, schedule)
        Log.e("Main_EditClick_schedule", "$schedule")
        scheduleAddEditResultLauncher.launch(intent)
    }

    fun openScheduleAddForResult() {
        val intent = Intent(this@ScheduleMainActivity, ScheduleAddEditActivity::class.java)
        intent.putExtra("key","addScheduleKey")
        scheduleAddEditResultLauncher.launch(intent)
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

    fun useAllSwitchClick(view: View) {
        val useAllSelected = !useAllButton.isSelected
        Log.e(TAG, "useAllSwitchClick: $useAllSelected")
        setUseAllSwitchState(useAllSelected)
//        useAllButton.setOnCheckedChangeListener { _, isChecked ->
//            setUseAllSwitchState(isChecked)
//            setScheduleInteraction(isChecked)
//        }
    }

    private fun checkUseAllSwitchState(value: String): Boolean = (value == "y")
    private fun setUseAllSwitchState(state: Boolean) {
        when (state) {
            true -> {
                useAllOnOff = true
                useAllButton.isSelected = true
                allSchedule!!.use_all = "y"
                useAll = "y"
                Log.e("setUseAllButton", "true:${useAllButton.isSelected}" )
//                binding.buttonUseAll.isSelected = true
            }
            false -> {
                useAllOnOff = false
                useAllButton.isSelected = false
                allSchedule!!.use_all = "n"
                useAll = "n"
                Log.e("setUseAllButton", "false:${useAllButton.isSelected}" )
//                useAllOnOff = false
//                binding.buttonUseAll.isSelected = false
            }
        }
        Log.e("setUseAllButton", "useAll:${allSchedule!!.use_all}")
    }


    private fun setScheduleListRecyclerViewInteraction() {
        scheduleListRecyclerView.addOnItemTouchListener(object: RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
        })
    }

    private fun setScheduleListRecyclerViewScroll() {
        scheduleListRecyclerView.layoutManager?.canScrollVertically()
    }

    private fun initRecyclerView() {
        Log.e(TAG, "initRecyclerView()")
//        // TODO add: getScheduleListFromServer
        scheduleListAdapter = ScheduleListAdapter()
//        scheduleListAdapter.data = allSchedule!!.schedules

        scheduleListRecyclerView.adapter = scheduleListAdapter
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_main)
        binding.activity = this@ScheduleMainActivity

        useAllButton = binding.buttonUseAll
        scheduleListRecyclerView = binding.recyclerviewScheduleList
    }
}