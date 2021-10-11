package com.android.jenny.scheduleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
    private lateinit var scheduleListRecyclerView: RecyclerView

    private lateinit var scheduleAddEditResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var useAllButton: ImageButton

    private var useAllOnOff: Boolean = true
    var useAll = "y"
    private var allSchedule: AllSchedule? = null
    private var schedules: MutableList<Schedule> = mutableListOf()

    companion object {
        private const val TAG = "ScheduleMainActivity"
        const val ADD_SCHEDULE_DATA = "addScheduleData"
        const val EDIT_SCHEDULE_DATA = "editScheduleData"
    }

    //TODO: Schedule array index 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
        initRecyclerView()
        getScheduleAllDataFromServer()
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
                        scheduleListAdapter.data[position!!] = data!!
                        scheduleListAdapter.notifyDataSetChanged()
                    }
                    "removeScheduleKey" -> {
                        Log.e(TAG, "remove_schedule_position: $position")
                        scheduleListAdapter.removeItem(position!!)
                    }
                }
                setScheduleListRecyclerView()
            }
        }

        scheduleListAdapter.setScheduleItemClickListener(object: ScheduleListAdapter.OnScheduleItemClickListener {
            override fun onScheduleItemClick(view: View, position: Int, schedule: Schedule) {
                openScheduleEditForResult(position,schedule)
            }
        })
   }

    private fun setScheduleListRecyclerView() {
        if (scheduleListAdapter.itemCount == 0) {
            binding.textviewScheduleListEmpty.visibility = View.VISIBLE
        } else {
            binding.textviewScheduleListEmpty.visibility = View.GONE
        }
    }

    private fun getScheduleAllDataFromServer() {
        //TODO: 1. Request Get Schedule From Server
        //TODO: 2. if result_status:0000 => loadScheduleAllDataFromServer() - Start
        val result_state = "0000"
        if (result_state == "0000") {
            loadScheduleAllDataFromServer()
            setScheduleListRecyclerView()
        } else {
            setScheduleListRecyclerView()
            setUseAllButtonState(true)
        }
    }

    private fun loadScheduleAllDataFromServer() {
        val allScheduleUse = "n"

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

        allSchedule = AllSchedule(allScheduleUse, schedules)
        scheduleListAdapter.data = allSchedule!!.schedules

        Log.e(TAG, "json_1:$schedules")
        Log.e(TAG, "json_2:$allSchedule")
        Log.e(TAG, "onResume()_useAllState: ${allSchedule!!.use_all}")
        setUseAllButtonState(checkUseAllSwitchState(allSchedule!!.use_all))
    }

    fun sendAllScheduleToServer() {
        Log.e(TAG, "sendAllScheduleToServer - start")
        makeAllScheduleJson()
    }

    private fun makeAllScheduleJson() {
        schedules = scheduleListAdapter.getScheduleItem()
        allSchedule = AllSchedule(useAll, schedules)
        val jsonScheduleList = JSONArray()
        for (i in schedules.indices) {
            val sObject = JSONObject()
            sObject.put("name", schedules[i].name)
            sObject.put("use", schedules[i].use)
            sObject.put("day", schedules[i].day)
            sObject.put("start", schedules[i].start)
            sObject.put("end", schedules[i].end)
            jsonScheduleList.put(sObject)
        }
        Log.e(TAG, "jsonScheduleList: $jsonScheduleList")

        val allScheduleJsonObject = JSONObject()
        allScheduleJsonObject.put("use_all", allSchedule!!.use_all)
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

    fun useAllButtonClick(view: View) {
        val useAllSelected = !useAllButton.isSelected
        Log.e(TAG, "useAllSwitchClick: $useAllSelected")
        setUseAllButtonState(useAllSelected)
    }

    private fun checkUseAllSwitchState(value: String): Boolean = (value == "y")
    private fun setUseAllButtonState(state: Boolean) {
        when (state) {
            true -> {
                useAllOnOff = true
                useAll = "y"
                useAllButton.isSelected = true
                setAllButtonEnabledTrue()
                Log.e("setUseAllButton", "true:${useAllButton.isSelected}" )
            }
            false -> {
                useAllOnOff = false
                useAll = "n"
                useAllButton.isSelected = false
                setAllButtonEnabledFalse()
                Log.e("setUseAllButton", "false:${useAllButton.isSelected}" )
            }
        }
        Log.e("setUseAllButton", "useAll:${useAll}")
    }

    private fun setAllButtonEnabledTrue() {

    }
    private fun setAllButtonEnabledFalse() {

    }

    private fun initRecyclerView() {
        Log.e(TAG, "initRecyclerView()")
        scheduleListAdapter = ScheduleListAdapter()
        scheduleListRecyclerView.adapter = scheduleListAdapter
//        scheduleListAdapter.data = allSchedule!!.schedules
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_main)
        binding.activity = this@ScheduleMainActivity

        useAllButton = binding.buttonScheduleUseAll
        scheduleListRecyclerView = binding.recyclerviewScheduleList
    }
}