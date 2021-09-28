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
import com.android.jenny.scheduleapp.databinding.ActivityScheduleListBinding
import com.android.jenny.scheduleapp.model.Schedule

class ScheduleListActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleListBinding
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var scheduleAddResultLauncher: ActivityResultLauncher<Intent>
//    private lateinit var scheduleEditResultLauncher: ActivityResultLauncher<Intent>

    private val list = mutableListOf<Schedule>()
    var mItemList: MutableList<Schedule> = ArrayList()
//    var mPosition: Int? = null

    companion object {
        private const val TAG = "ScheduleListActivity"
        const val ADD_SCHEDULE_DATA = "addScheduleData"
        const val EDIT_SCHEDULE_DATA = "editScheduleData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initRecyclerView()
        var mPosition:Int? = null

        scheduleAddResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val key = result.data?.getStringExtra("key")
                Log.e(TAG, "key: $key")
                when (key) {
//                    "removeScheduleKey" -> {
//                        scheduleListAdapter.removeItem(mPosition!!)
//                    }
                    "addScheduleKey" -> {
                        val data = result.data?.getParcelableExtra<Schedule>(ADD_SCHEDULE_DATA)
                        Log.e(TAG, "add_schedule_data: $data")
                        scheduleListAdapter.addItem(data!!)
                    }
//                    "editScheduleKey" -> {
//                        val data = result.data?.getParcelableExtra<Schedule>(EDIT_SCHEDULE_DATA)
//                        Log.e(TAG, "edit_schedule_data: $data")
//                    }
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

//        scheduleEditResultLauncher =  registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == RESULT_OK) {
//                val key = it.data?.getStringExtra("key")
//                Log.e(TAG, "key: $key")
//                when (key) {
//                    "removeScheduleKey" -> {
//                        scheduleListAdapter.removeItem(mPosition!!)
//                    }
//                    "editScheduleKey" -> {
//                        val data = it.data?.getParcelableExtra<Schedule>(EDIT_SCHEDULE_DATA)
//                        Log.e(TAG, "edit_schedule_data(): $data")
//                        mItemList[mPosition!!] = data!!
////                        scheduleListAdapter.notifyDataSetChanged()
//                    }
//                }
//                    scheduleListAdapter.notifyDataSetChanged()
//            }
//            }

//        scheduleListAdapter.setItemClickListener(object: ScheduleListAdapter.OnItemClickListener {
//            override fun onClick(view: View, position: Int) {
//                val item = mItemList[position]
//                Toast.makeText(view.context,
//                    "${item.name}\n${item.start_time}-${item.end_time}",
//                    Toast.LENGTH_LONG).show()
//                scheduleListAdapter.notifyDataSetChanged()
//            }
//        })
    }

    fun openScheduleEditForResult(position: Int, schedule: Schedule) {
        val intent = Intent(this@ScheduleListActivity, ScheduleAddActivity::class.java)
        intent.putExtra("key","editScheduleKey")
        intent.putExtra("position", position)
        intent.putExtra(EDIT_SCHEDULE_DATA, schedule)
        startActivity(intent)
//        scheduleAddResultLauncher.launch(intent)
    }

//    private fun resultForScheduleAdd(): ActivityResultLauncher<Intent> =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val key = result.data?.getStringExtra("key")
//                Log.e(TAG, "key: $key")
//                when (result.data?.getStringExtra("key")) {
//                    "removeScheduleKey" -> {
//                        scheduleListAdapter.removeItem(mPosition!!)
//                    }
//                    "addScheduleKey" -> {
//                        val data = result.data?.getParcelableExtra<Schedule>(ADD_SCHEDULE_DATA)
//                        Log.e(TAG, "add_schedule_data: $data")
//                        scheduleListAdapter.addItem(data!!)
//                    }
//                }
//            }
//        }

    fun openScheduleAddForResult() {
        val intent = Intent(this@ScheduleListActivity, ScheduleAddActivity::class.java)
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