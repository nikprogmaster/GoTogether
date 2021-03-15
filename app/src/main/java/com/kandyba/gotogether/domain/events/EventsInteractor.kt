package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Single

interface EventsInteractor {

    fun getEventsRecommendations(token: String): Single<List<EventModel>>

    fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel>
}