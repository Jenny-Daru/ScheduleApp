package com.android.jenny.scheduleapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.ModifyActivity
import com.android.jenny.scheduleapp.databinding.ItemScheduleBinding
import com.android.jenny.scheduleapp.model.Schedule


class ScheduleRecyclerAdapter(): RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder>() {

    var data = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleRecyclerAdapter.ScheduleViewHolder {
        return ScheduleViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun getItemCount(): Int = data.size

    inner class ScheduleViewHolder(var binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.textviewScheduleTitle.text = schedule.name
            binding.textviewScheduleTime.text = schedule.start_time.plus(" - ").plus(schedule.end_time)
        }
    }

    override fun onBindViewHolder(holder: ScheduleRecyclerAdapter.ScheduleViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.binding.buttonModifyDetail.setOnClickListener {
            var intent = Intent(it.context, ModifyActivity::class.java)
            intent.putExtra("modifySchedule", item)
            it.context.startActivity(intent)
        }

        holder.apply { bind(item) }
//        holder.bind(data[position])
    }


    interface OnItemClickListener {
        fun onClick(view:View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}