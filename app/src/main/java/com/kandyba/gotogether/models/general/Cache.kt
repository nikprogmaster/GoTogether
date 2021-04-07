package com.kandyba.gotogether.models.general

import com.kandyba.gotogether.models.presentation.EventModel

private const val NAME = "events"

class Cache {

    private val cache = HashMap<String, List<EventModel>>()

    fun getCache(): List<EventModel>? {
        return if (cache.isNotEmpty()) cache[NAME]
        else null
    }

    fun saveCache(events: List<EventModel>?) {
        events?.apply {
            cache[NAME] = events
        }
    }

    companion object {
        val instance = Cache()
    }
}
