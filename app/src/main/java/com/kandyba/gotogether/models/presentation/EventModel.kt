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

    fun getStartCalendarDate(): Calendar {
        val date = getAnchorDate()
        date.timeInMillis = date.timeInMillis + startUnix.toLong() * 1000
        return date
    }

    fun getStartCalendarDay(): String {
        return getStartCalendarDate().get(Calendar.DAY_OF_MONTH).toString()
    }

    fun getStartCalendarMonth(): String {
        return getStartCalendarDate().get(Calendar.MONTH).toString()
    }

    private fun getAnchorDate(): Calendar {
        calendarInstance.set(1970, 1, 0, 0, 0, 0)
        calendarInstance.set(Calendar.MILLISECOND, 0)
        calendarInstance.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset)
        return calendarInstance
    }

    companion object {
        private val calendarInstance = Calendar.getInstance(TimeZone.getDefault())
    }

}

class Events(val events: List<EventModel>): Serializable