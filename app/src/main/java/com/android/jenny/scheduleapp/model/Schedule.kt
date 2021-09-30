package com.android.jenny.scheduleapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AllSchedule (
    val user_all: String,
    val schedules: MutableList<Schedule> = mutableListOf()
): Parcelable

@Parcelize
@Serializable
data class Schedule (
    var name: String,
    var use: String,
    var day: String,
    var start: String,
    var end: String
): Parcelable
