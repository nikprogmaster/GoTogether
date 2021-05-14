package com.kandyba.gotogether.models.domain.events

import java.io.Serializable

/**
 * Модель даты события (domain-слой)
 */
data class DateDomainModel(
    val startUnix: Long,
    val endUnix: Long
) : Serializable