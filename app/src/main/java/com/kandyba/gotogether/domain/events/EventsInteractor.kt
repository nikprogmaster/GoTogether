package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.domain.events.EventComplainDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Completable
import io.reactivex.Single

interface EventsInteractor {

    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>>

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