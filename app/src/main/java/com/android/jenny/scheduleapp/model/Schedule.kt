package com.android.jenny.scheduleapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllSchedule (
    val all_on_off: String,
    val data: List<Schedule>
): Parcelable

@Parcelize
data class Schedule (
    val on_off: String,
    val name: String,
    val day: String,
    val start_time: String,
    val end_time: String
): Parcelable

