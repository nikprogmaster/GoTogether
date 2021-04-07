package com.kandyba.gotogether.models.presentation

import java.util.*

fun getAnchorDate(): Calendar {
    val calendarInstance = Calendar.getInstance(TimeZone.getDefault())
    calendarInstance.set(1970, 1, 0, 0, 0, 0)
    calendarInstance.set(Calendar.MILLISECOND, 0)
    calendarInstance.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset)
    return calendarInstance
}

fun getCalendarDate(unixTime: Long): Calendar {
    val date = getAnchorDate()
    date.timeInMillis = date.timeInMillis + unixTime * 1000
    return date
}

fun getFormattedDate(unixTime: Long): String {
    val date = getCalendarDate(unixTime)
    val result =
        "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"
    return result
}

fun getFormattedTime(unixTime: Long): String {
    val date = getCalendarDate(unixTime)
    val result = "${date.get(Calendar.HOUR_OF_DAY)}:${getMinutes(date)}"
    return result
}

private fun getMinutes(date: Calendar): String {
    val minute = date.get(Calendar.MINUTE)
    return if (minute > 9) {
        "$minute"
    } else {
        "0$minute"
    }
}

fun getCalendarDay(unixTime: Long): String {
    return getCalendarDate(unixTime).get(Calendar.DAY_OF_MONTH).toString()
}

fun getDayOfWeek(unixTime: Long): String? {
    return getCalendarDate(unixTime).getDisplayName(
        Calendar.DAY_OF_WEEK,
        Calendar.SHORT,
        Locale.getDefault()
    )?.toUpperCase(Locale.getDefault())
}

fun getMonth(unixTime: Long): String =
    when (getCalendarDate(unixTime).get(Calendar.MONTH)) {
        0 -> "Января"
        1 -> "Февраля"
        2 -> "Марта"
        3 -> "Апреля"
        4 -> "Мая"
        5 -> "Июня"
        6 -> "Июля"
        7 -> "Августа"
        8 -> "Сентября"
        9 -> "Октября"
        10 -> "Ноября"
        else -> "Декабря"
    }

