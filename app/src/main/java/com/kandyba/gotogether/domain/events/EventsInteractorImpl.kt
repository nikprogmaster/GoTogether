package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.data.repository.EventsRepository
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Completable
import io.reactivex.Single

class EventsInteractorImpl(
    private val eventsRepository: EventsRepository,
    private val eventsDomainConverter: EventsDomainConverter
): EventsInteractor {

    override fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>> {
        return if (Cache.instance.getCache() == null) {
            eventsRepository.getEventsRecommendations(token, amount)
                .map { eventsDomainConverter.convert(it) }
                .doAfterSuccess { Cache.instance.saveCache(it) }
        } else {
            Single.just(Cache.instance.getCache())
        }
    }

    override fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel> {
        return eventsRepository.getEventInfo(token, eventId)
    }

    override fun complainEvent(
        token: String,
        eventId: String,
        body: EventComplaintRequestBody
    ): Completable {
        return eventsRepository.complainEvent(token, eventId, body)
    }

    override fun participateInEvent(
        token: String,
        eventId: String
    ): Completable {
        return eventsRepository.participateInEvent(token, eventId)
    }

    override fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventModel>> {
        return eventsRepository.searchEventsByInterests(token, interests)
            .map { eventsDomainConverter.convert(it) }
    }

    override fun searchEventsByTextQuery(
        token: String,
        text: String
    ): Single<List<EventModel>> {
        return eventsRepository.searchEventsByTextQuery(token, text)
            .map { eventsDomainConverter.convert(it) }
    }
}