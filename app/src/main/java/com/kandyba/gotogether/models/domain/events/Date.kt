package com.kandyba.gotogether.models.domain.events

import java.io.Serializable

data class Date(
    val startUnix: String,
    val endUnix: String
) : Serializable