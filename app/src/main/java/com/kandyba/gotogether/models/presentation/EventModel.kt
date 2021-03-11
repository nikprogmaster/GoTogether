package com.kandyba.gotogether.models.presentation

import java.io.Serializable
import java.util.*

data class EventModel(
    val title: String,
    val photoLinks: List<String>,
    val likedByUser: Boolean,
    val dates: List<Date>,
    val price: String,
    val isFree: Boolean,
    val categories: List<String>
): Serializable

data class Date(
    val startUnix: String,
    val endUnix: String
): Serializable {

    private fun getStartCalendarDate(): Calendar {
        val date = getAnchorDate()
        date.timeInMillis = date.timeInMillis + startUnix.toLong() * 1000
        return date
    }

    fun getStartCalendarDay(): String {
        return getStartCalendarDate().get(Calendar.DAY_OF_MONTH).toString()
    }

    fun getStartCalendarMonth(): Int {
        return getStartCalendarDate().get(Calendar.MONTH)
    }
}

class Events(val events: List<EventModel>): Serializable