package com.kandyba.gotogether.models.presentation

import java.util.*

/*fun getAnchorDate(): Calendar {
    val calendarInstance = Calendar.getInstance(TimeZone.getDefault())
    calendarInstance.set(1970, 0, 0, 0, 0, 0)
    calendarInstance.set(Calendar.MILLISECOND, 0)
    calendarInstance.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset)
    return calendarInstance
}*/

fun getCalendarDate(unixTime: Long): Calendar {
    val date = Calendar.getInstance()
    date.timeInMillis = unixTime
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
    val now = Calendar.getInstance().setStartDayValues()
    val birthday = Calendar.getInstance()
    birthday.timeInMillis = unixTime
    birthday.setStartDayValues()
    val delta = now.timeInMillis - birthday.timeInMillis
    var days = convertInDays(delta)
    val leapYearsAmount = countLeapYears(now[Calendar.YEAR], birthday[Calendar.YEAR])
    days -= (leapYearsAmount + 1)
    val yearResult: Int = (days / 365).toInt()

    var str = when (yearResult % 10) {
        1 -> "год"
        2, 3, 4 -> "года"
        else -> "лет"
    }
    if (yearResult == 11 || yearResult == 12 || yearResult == 13 || yearResult == 14) {
        str = "лет"
    }
    return "$yearResult $str"
}

private fun Calendar.setStartDayValues(): Calendar {
    this.set(Calendar.HOUR, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
    return this
}

private fun convertInDays(timeInMillis: Long): Double {
    return (timeInMillis / 1000 / 60 / 60 / 24).toDouble()
}

private fun countLeapYears(startYear: Int, endYear: Int): Int {
    var counter = 0
    if (startYear != endYear) {
        for (year in startYear..endYear) {
            if (isLeapYear(year)) counter++
        }
    } else {
        return 1
    }
    return counter
}

private fun isLeapYear(year: Int): Boolean {
    val divider = if (year % 100 == 0) year % 400 else year % 4
    return divider == 0
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

