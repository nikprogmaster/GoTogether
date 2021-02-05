package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.data.repository.EventsRepository
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Single

class EventsInteractorImpl(
    private val eventsRepository: EventsRepository,
    private val eventsDomainConverter: EventsDomainConverter
): EventsInteractor {

    override fun getEventsRecommendations(token: String): Single<Map<String, EventModel>> {
        return eventsRepository.getEventsRecommendations(token)
            .map { eventsDomainConverter.convert(it) }
    }
}