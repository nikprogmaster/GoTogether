package com.kandyba.gotogether.models.presentation

import java.time.LocalDate
import java.time.Period
import java.util.*

fun getAnchorDate(): Calendar {
    val calendarInstance = Calendar.getInstance(TimeZone.getDefault())
    calendarInstance.set(1970, 0, 0, 0, 0, 0)
    calendarInstance.set(Calendar.MILLISECOND, 0)
    calendarInstance.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset)
    return calendarInstance
}

fun getCalendarDate(unixTime: Long): Calendar {
    val date = getAnchorDate()
    date.timeInMillis = date.timeInMillis + unixTime
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

fun getTodayTimeOrDate(unixTime: Long): String {
    val date = Calendar.getInstance()
    date.timeInMillis = unixTime
    return if (isToday(date)) {
        getFormattedTime(unixTime)
    } else {
        getShortDate(unixTime)
    }
}

private fun isToday(date: Calendar): Boolean {
    val today = Calendar.getInstance()
    return today[Calendar.YEAR] == date[Calendar.YEAR]
            && today[Calendar.MONTH] == date[Calendar.MONTH]
            && today[Calendar.DAY_OF_MONTH] == date[Calendar.DAY_OF_MONTH]
}

private fun getMinutes(date: Calendar): String {
    val minute = date.get(Calendar.MINUTE)
    return if (minute > 9) {
        "$minute"
    } else {
        "0$minute"
    }
}

fun getAge(unixTime: Long): String {
    val date = Calendar.getInstance()
    date.timeInMillis = unixTime
    val birthDate = LocalDate.of(
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH)
    )
    val now = LocalDate.now()
    val delta = Period.between(birthDate, now)
    var str = when (delta.years % 10) {
        1 -> "год"
        2, 3, 4 -> "года"
        else -> "лет"
    }
    if (delta.years == 11 || delta.years == 12 || delta.years == 13 || delta.years == 14) {
        str = "лет"
    }
    return "${delta.years} $str"
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

private fun getShortDate(unixTime: Long): String {
    val date = getCalendarDate(unixTime)
    return "${date[Calendar.DAY_OF_MONTH]} ${
        date.getDisplayName(
            Calendar.MONTH,
            Calendar.SHORT_STANDALONE,
            Locale.getDefault()
        )
    }"
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

