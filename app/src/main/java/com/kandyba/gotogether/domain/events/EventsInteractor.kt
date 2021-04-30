package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Completable
import io.reactivex.Single

interface EventsInteractor {

    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>>

    fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel>

    fun complainEvent(token: String, eventId: String, body: EventComplaintRequestBody): Completable

    fun participateInEvent(token: String, eventId: String): Completable

    fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventModel>>

    fun searchEventsByTextQuery(token: String, text: String): Single<List<EventModel>>


}