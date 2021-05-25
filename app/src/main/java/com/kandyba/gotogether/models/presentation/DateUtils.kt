package com.kandyba.gotogether.models.presentation

import android.util.Log
import java.util.*

/**
 * Получить дату, используя UTC время
 *
 * @param unixTime время в миллисекундах
 * @return [Calendar]
 */
fun getCalendarDate(unixTime: Long): Calendar {
    val date = Calendar.getInstance()
    date.timeInMillis = unixTime
    return date
}

/**
 * Получить дату в формате dd.mm.yyyy
 *
 * @param unixTime время в миллисекундах
 * @return [String] дата
 */
fun getFormattedDate(unixTime: Long): String {
    val date = getCalendarDate(unixTime * 1000)
    return "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH) + 1}.${date.get(Calendar.YEAR)}"
}

/**
 * Получить время в формате hh:mm
 *
 * @param unixTime время в миллисекундах
 * @return [String] время
 */
fun getFormattedTime(unixTime: Long): String {
    val date = getCalendarDate(unixTime * 1000)
    val result = "${date.get(Calendar.HOUR_OF_DAY)}:${getMinutes(date)}"
    return result
}

/**
 * Получить время сегодняшнего дня, либо дату
 *
 * @param unixTime время в миллисекундах
 * @return [String] время, либо дата
 */
fun getTodayTimeOrDate(unixTime: Long): String {
    val date = Calendar.getInstance()
    date.timeInMillis = unixTime
    return if (isToday(date)) {
        getFormattedTime(unixTime)
    } else {
        getShortDate(unixTime)
    }
}

private fun isToday(date: Calendar?): Boolean {
    val today = Calendar.getInstance()
    return if (date == null) false
    else (today[Calendar.YEAR] == date[Calendar.YEAR]
            && today[Calendar.MONTH] == date[Calendar.MONTH]
            && today[Calendar.DAY_OF_MONTH] == date[Calendar.DAY_OF_MONTH])
}

private fun getMinutes(date: Calendar): String {
    val minute = date.get(Calendar.MINUTE)
    return if (minute > 9) {
        "$minute"
    } else {
        "0$minute"
    }
}

/**
 * Посчитать возраст по дате рождения с учетом високосных лет
 *
 * @param unixTime время в миллисекундах
 * @return [String] возраст
 */
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

/**
 * Получить день по UTC времени
 *
 * @param unixTime время в миллисекундах
 * @return [String] день
 */
fun getCalendarDay(unixTime: Long): String {
    val realTimeInMillis = unixTime * 1000
    return getCalendarDate(realTimeInMillis).get(Calendar.DAY_OF_MONTH).toString()
}

/**
 * Получить день недели по UTC времени
 *
 * @param unixTime время в миллисекундах
 * @return [String] день недели
 */
fun getDayOfWeek(unixTime: Long): String? {
    return getCalendarDate(unixTime * 1000).getDisplayName(
        Calendar.DAY_OF_WEEK,
        Calendar.SHORT,
        Locale.getDefault()
    )?.toUpperCase(Locale.getDefault())
}

private fun getShortDate(unixTime: Long): String {
    val date = getCalendarDate(unixTime)
    return "${date[Calendar.DAY_OF_MONTH]} ${getMonth(unixTime / 1000)}"
}

/**
 * Получить месяц по UTC времени
 *
 * @param unixTime время в миллисекундах
 * @return [String] месяц
 */
fun getMonth(unixTime: Long): String {
    val realTimeInMillis = unixTime * 1000
    return when (getCalendarDate(realTimeInMillis).get(Calendar.MONTH)) {
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
}

/**
 * Распарсить дату из строки
 *
 * @param date строка в формате dd.mm.yyyy
 * @return [Calendar] дата
 */
fun parseDate(date: String): Calendar? {
    if (date.length != 10) {
        return null
    }
    return try {
        val day = date.substring(0, 2).toInt()
        val year = date.substring(6, 10).toInt()
        val monthStart = if (date[3].toString() == "0") 4 else 3
        val month = date.substring(monthStart, 5).toInt()
        val result = Calendar.getInstance().setStartDayValues()
        result.set(Calendar.YEAR, year)
        result.set(Calendar.MONTH, month)
        result.set(Calendar.DAY_OF_MONTH, day)
        if (isThisYearOrFuture(result)) null else result
    } catch (e: NumberFormatException) {
        Log.e("NumberFormatException:", "parseDate() called with: date = $date")
        null
    }
}

/**
 * Проверка на этот год или будущую дату
 *
 * @param date проверяемая дата
 * @return true если это сегодняшняя или будущая дата, иначе false
 */
private fun isThisYearOrFuture(date: Calendar): Boolean {
    val today = Calendar.getInstance().setStartDayValues()
    today.set(Calendar.YEAR, today.get(Calendar.YEAR) - 1)
    return date.timeInMillis >= today.timeInMillis
}


