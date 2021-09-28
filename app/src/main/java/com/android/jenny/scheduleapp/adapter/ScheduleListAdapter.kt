package com.android.jenny.scheduleapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.ScheduleAddActivity
import com.android.jenny.scheduleapp.ScheduleListActivity
import com.android.jenny.scheduleapp.databinding.ItemScheduleBinding
import com.android.jenny.scheduleapp.model.Schedule


class ScheduleListAdapter(): RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {

    var data = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListAdapter.ScheduleViewHolder {
        return ScheduleViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ScheduleListAdapter.ScheduleViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)

//        holder.binding.buttonEdit.setOnClickListener {
//            editClickListener.editClick(it, position, item)
//        }

    }

    fun addItem(schedule: Schedule) {
        data.add(schedule)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position > 0) {
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }

    inner class ScheduleViewHolder(var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule, position: Int) {
            binding.textviewScheduleName.text = schedule.name
            binding.textviewScheduleTime.text = schedule.start_time.plus(" - ").plus(schedule.end_time)

//            binding.buttonEdit.setOnClickListener {
//                val intent = Intent(it.context, ScheduleAddActivity::class.java)
//                intent.putExtra("key","editScheduleKey")
//                intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, schedule)
//                intent.putExtra("position", position)
//                it.context.startActivity(intent)
//            }

            binding.buttonEdit.setOnClickListener {
                editClickListener.editClick(position, schedule)
//                val intent = Intent(it.context, ScheduleAddActivity::class.java)
//                intent.putExtra("key", "editScheduleKey")
//                intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, schedule)
//                it.context.startActivity(intent)
            }
        }
    }

    interface EditClickListener {
        fun editClick(position: Int, schedule: Schedule)
    }

    fun setOnEditClickListener(editClickListener: EditClickListener) {
        this.editClickListener = editClickListener
    }

    private lateinit var editClickListener: EditClickListener


    interface OnItemClickListener {
        fun onClick(view:View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener


}