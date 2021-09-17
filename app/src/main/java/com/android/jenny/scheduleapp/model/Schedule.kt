package com.android.jenny.scheduleapp.model

data class AllSchedule (
    val all_on_off: String,
    val data: List<Schedule>
)

data class Schedule (
    val on_off: String,
    val name: String,
    val day: String,
    val start_time: String,
    val end_time: String
)

