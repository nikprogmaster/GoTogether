package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Single

interface EventsInteractor {

    fun getEventsRecommendations(token: String): Single<Map<String, EventModel>>
}