package com.android.jenny.scheduleapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.jenny.scheduleapp.ScheduleEditActivity
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
        holder.bind(item)

        holder.itemView.setOnClickListener {
            setPosition(position)
//            itemClickListener.onClick(it, position)
        }

        holder.binding.buttonModifyDetail.setOnClickListener {
            var intent = Intent(it.context, ScheduleEditActivity::class.java)
            intent.putExtra("modifySchedule", item)
            it.context.startActivity(intent)
        }
//        holder.bind(data[position])
    }

    var mPosition = 0

    fun getPosition(): Int {
        return mPosition
    }

    private fun setPosition(position: Int) {
        mPosition = position
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
        fun bind(schedule: Schedule) {
            binding.textviewScheduleName.text = schedule.name
            binding.textviewScheduleTime.text = schedule.start_time.plus(" - ").plus(schedule.end_time)
        }
    }

    interface OnItemClickListener {
        fun onClick(view:View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}