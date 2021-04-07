package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import io.reactivex.Completable
import io.reactivex.Single

interface EventsRepository {

    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventDetailsDomainModel>>

    fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel>

    fun complainEvent(token: String, eventId: String, body: EventComplaintRequestBody): Completable

    fun participateInEvent(token: String, eventId: String): Completable

    fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventDetailsDomainModel>>

    fun searchEventsByTextQuery(token: String, text: String): Single<List<EventDetailsDomainModel>>
}