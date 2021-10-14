package com.android.jenny.scheduleapp.adapter

import android.content.res.Resources
import android.graphics.Color
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.R
import com.android.jenny.scheduleapp.databinding.ItemScheduleBinding
import com.android.jenny.scheduleapp.model.AllSchedule
import com.android.jenny.scheduleapp.model.Schedule
import java.lang.IndexOutOfBoundsException

class ScheduleListAdapter(): RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {

    var data = mutableListOf<Schedule>()

    fun addItem(schedule: Schedule) {
        data.add(schedule)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        try {
            data.removeAt(position)
            notifyDataSetChanged()
//            notifyItemChanged(position, data.size)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            Log.e("ScheduleListAdapter","${e.message}")
        }
    }

    fun getScheduleItem(): MutableList<Schedule> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListAdapter.ScheduleViewHolder {
        return ScheduleViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ScheduleListAdapter.ScheduleViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }

    inner class ScheduleViewHolder(private var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule, position: Int) {
            binding.textviewScheduleName.text = schedule.name
            binding.textviewScheduleDays.text = schedule.day.uppercase()
            binding.textviewScheduleTime.text = schedule.start.plus(" - ").plus(schedule.end)

            Log.e("ScheduleListAdapter", schedule.use)
            when (schedule.use) {
                "y" -> {
                    binding.buttonEachOnOff.isSelected = true
                    binding.textviewEachOnOff.setText(R.string.schedule_on)
                    binding.textviewEachOnOff.setTextColor(Color.rgb(141,183,74))
                }
                "n" -> {
                    binding.buttonEachOnOff.isSelected = false
                    binding.textviewEachOnOff.setText(R.string.schedule_off)
                    binding.textviewEachOnOff.setTextColor(Color.rgb(137,137,137))
                }
            }

            binding.itemView.setOnClickListener {
                // ToDo: Item 클릭 시 버튼 색 변경
                binding.buttonEdit.isPressed = true
                sItemClickListener.onScheduleItemClick(it, position, schedule)
            }

//            binding.buttonEdit.setOnClickListener {
//                editClickListener.editClick(position, schedule)
//            }
        }
    }

    interface OnScheduleItemClickListener {
        fun onScheduleItemClick(view: View, position: Int, schedule: Schedule)
    }

    fun setScheduleItemClickListener(scheduleItemClickListener: OnScheduleItemClickListener) {
        this.sItemClickListener = scheduleItemClickListener
    }

    private lateinit var sItemClickListener: OnScheduleItemClickListener

    interface EditClickListener {
        fun editClick(position: Int, schedule: Schedule)
    }

    fun setOnEditClickListener(editClickListener: EditClickListener) {
        this.editClickListener = editClickListener
    }

    private lateinit var editClickListener: EditClickListener

}