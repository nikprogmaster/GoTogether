package com.kandyba.gotogether.models.presentation

import java.util.*

fun getAnchorDate(): Calendar {
    val calendarInstance = Calendar.getInstance(TimeZone.getDefault())
    calendarInstance.set(1970, 1, 0, 0, 0, 0)
    calendarInstance.set(Calendar.MILLISECOND, 0)
    calendarInstance.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset)
    return calendarInstance
}

fun getMonth(month: Int): String =
    when (month) {
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

