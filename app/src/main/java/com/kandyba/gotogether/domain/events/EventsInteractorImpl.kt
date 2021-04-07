package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.data.repository.EventsRepository
import com.kandyba.gotogether.models.domain.events.EventComplainDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Completable
import io.reactivex.Single

class EventsInteractorImpl(
    private val eventsRepository: EventsRepository,
    private val eventsDomainConverter: EventsDomainConverter
): EventsInteractor {

    override fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>> {
        return eventsRepository.getEventsRecommendations(token, amount)
            .map { eventsDomainConverter.convert(it) }
    }

    override fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel> {
        return eventsRepository.getEventInfo(token, eventId)
    }

    override fun eventComplain(
        token: String,
        body: EventComplaintRequestBody
    ): Single<EventComplainDomainModel> {
        return eventsRepository.eventComplain(token, body)
    }

    override fun participateInEvent(
        token: String,
        body: ParticipationRequestBody,
        eventId: String
    ): Completable {
        return eventsRepository.participateInEvent(token, body, eventId)
    }
}