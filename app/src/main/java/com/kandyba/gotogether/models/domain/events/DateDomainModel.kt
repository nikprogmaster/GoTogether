package com.kandyba.gotogether.models.domain.events

import java.io.Serializable

data class DateDomainModel(
    val startUnix: String,
    val endUnix: String
) : Serializable