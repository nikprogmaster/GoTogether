package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.events.EventComplainDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import io.reactivex.Completable
import io.reactivex.Single

interface EventsRepository {

    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventDetailsDomainModel>>

    fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel>

    fun eventComplain(
        token: String,
        body: EventComplaintRequestBody
    ): Single<EventComplainDomainModel>

    fun participateInEvent(
        token: String,
        body: ParticipationRequestBody,
        eventId: String
    ): Completable
}