package com.android.jenny.scheduleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    }

    fun addItem(schedule: Schedule) {
        data.add(schedule)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
            data.removeAt(position)
            notifyDataSetChanged()
            notifyItemRangeChanged(position, data.size)
    }

    inner class ScheduleViewHolder(var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule, position: Int) {
            binding.textviewScheduleName.text = schedule.name
            binding.textviewScheduleDays.text = schedule.day
            binding.textviewScheduleTime.text = schedule.start_time.plus(" - ").plus(schedule.end_time)

            binding.buttonEdit.setOnClickListener {
                editClickListener.editClick(position, schedule)
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

}