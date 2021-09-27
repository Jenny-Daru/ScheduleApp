package com.android.jenny.scheduleapp

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.adapter.ScheduleListAdapter
import com.android.jenny.scheduleapp.databinding.ActivityScheduleListBinding
import com.android.jenny.scheduleapp.model.Schedule

class ScheduleListActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleListBinding
    private lateinit var scheduleActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var scheduleListAdapter: ScheduleListAdapter

    private val list = mutableListOf<Schedule>()
    var mItemList: MutableList<Schedule> = ArrayList()
    var mPosition: Int? = null

    init {
        instance = this
    }

    companion object {
        private const val TAG = "ScheduleActivity"
        const val ADD_SCHEDULE_DATA = "addScheduleData"
        const val EDIT_SCHEDULE_DATA = "editScheduleData"

        private var instance: ScheduleListActivity? = null
        fun getInstance(): ScheduleListActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initRecyclerView()

        scheduleActivityResultLauncher = resultForScheduleAddActivity()

        scheduleListAdapter.setOnEditClickListener(object: ScheduleListAdapter.EditClickListener{
            override fun editClick(view: View, position: Int, schedule: Schedule) {

            }
        })

        scheduleListAdapter.setItemClickListener(object: ScheduleListAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val item = mItemList[position]
                Toast.makeText(view.context,
                    "${item.name}\n${item.start_time}-${item.end_time}",
                    Toast.LENGTH_LONG).show()
                scheduleListAdapter.notifyDataSetChanged()
            }
        })
    }

    fun editSchedule(position: Int, schedule: Schedule) {
        val intent = Intent(this@ScheduleListActivity, ScheduleAddActivity::class.java)
        mPosition = position
        intent.putExtra("key","editScheduleKey")
        intent.putExtra(EDIT_SCHEDULE_DATA, schedule)
        scheduleActivityResultLauncher.launch(intent)
    }

    private fun resultForScheduleAddActivity(): ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val key = result.data?.getStringExtra("key")
                if (key.equals("editScheduleKey")) {
                    val data = result.data?.getParcelableExtra<Schedule>(EDIT_SCHEDULE_DATA)
                    Log.e(TAG, "edit_schedule_data(): $data")
                    mItemList[mPosition!!] = data!!
                    scheduleListAdapter.notifyDataSetChanged()
                } else {
                    val data = result.data?.getParcelableExtra<Schedule>(ADD_SCHEDULE_DATA)
                    Log.e(TAG, "add_schedule_data")
                    scheduleListAdapter.addItem(data!!)
                }
            }
        }

    fun openScheduleAddActivityForResult() {
        val intent = Intent(this@ScheduleListActivity, ScheduleAddActivity::class.java)
        scheduleActivityResultLauncher.launch(intent)
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



//    private fun initScheduleList() {
//        // TODO add: getScheduleListFromServer
//        Log.e(TAG, "initScheduleAdapter()")
//        scheduleListAdapter = ScheduleListAdapter()
//        scheduleListAdapter.data = mItemList
//
//        scheduleListAdapter.setItemClickListener(object: ScheduleListAdapter.OnItemClickListener {
//            override fun onClick(view: View, position: Int) {
//                val item = mItemList[position]
//                Toast.makeText(view.context,
//                    "${item.name}\n${item.start_time}-${item.end_time}",
//                    Toast.LENGTH_LONG).show()
//                scheduleListAdapter.notifyDataSetChanged()
//            }
//        })
//
//        binding.recyclerviewScheduleList.adapter = scheduleListAdapter
//        Log.e(TAG, "init_mItemList: $mItemList")
////        scheduleAdapter.notifyDataSetChanged()
//
////        list.apply {
////            add(data)
////        }
//    }
}