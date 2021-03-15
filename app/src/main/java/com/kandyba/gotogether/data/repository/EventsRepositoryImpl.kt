package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import io.reactivex.Single

class EventsRepositoryImpl(
    private val eventsApiMapper: EventsApiMapper,
    private val eventsDataConverter: EventsDataConverter,
    private val eventDetailsDataConverter: EventDetailsDataConverter
): EventsRepository {

    override fun getEventsRecommendations(token: String): Single<Map<String, EventInfoDomainModel>> {
        val tokenResult = TOKEN_PREFIX + token
        return eventsApiMapper.getEventsRecommendations(tokenResult)
            .map { eventsDataConverter.convert(it.events) }
    }

    override fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel> {
        val tokenResult = TOKEN_PREFIX + token
        return eventsApiMapper.getEventInfo(tokenResult, eventId)
            .map { eventDetailsDataConverter.convert(it) }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}