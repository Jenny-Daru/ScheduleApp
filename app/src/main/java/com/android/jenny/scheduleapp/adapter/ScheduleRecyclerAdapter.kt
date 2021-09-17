package com.android.jenny.scheduleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.databinding.ItemScheduleBinding
import com.android.jenny.scheduleapp.model.Schedule


class ScheduleRecyclerAdapter(private var data: List<Schedule>): RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleRecyclerAdapter.ScheduleViewHolder {
        return ScheduleViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun onBindViewHolder(
        holder: ScheduleRecyclerAdapter.ScheduleViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ScheduleViewHolder(private var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(dataInfo: Schedule) {
            binding.textviewScheduleTitle.text = dataInfo.name
            binding.textviewScheduleDay.text = dataInfo.day
            binding.textviewScheduleTime.text = dataInfo.start_time.plus(":").plus(dataInfo.end_time)
        }
    }


}