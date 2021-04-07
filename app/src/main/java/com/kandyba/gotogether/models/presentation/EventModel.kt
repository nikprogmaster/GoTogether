package com.kandyba.gotogether.models.presentation

import java.io.Serializable

data class EventModel(
    val id: String,
    val title: String,
    val photoLinks: List<String>,
    var likedByUser: Boolean,
    val dates: List<Date>?,
    val price: String?,
    val isFree: Boolean?,
    val categories: List<String>?,
    var activated: Boolean
): Serializable

data class Date(
    val startUnix: String,
    val endUnix: String
) : Serializable

class Events(val events: List<EventModel>): Serializable