package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import io.reactivex.Completable
import io.reactivex.Single

class EventsRepositoryImpl(
    private val eventsApiMapper: EventsApiMapper,
    private val eventsDataConverter: EventsDataConverter,
    private val eventDetailsDataConverter: EventDetailsDataConverter
): EventsRepository {

    override fun getEventsRecommendations(
        token: String,
        amount: Int
    ): Single<List<EventDetailsDomainModel>> {
        return eventsApiMapper.getEventsRecommendations(token, 10)
            .map { eventsDataConverter.convert(it) }
    }

    override fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel> {
        return eventsApiMapper.getEventInfo(token, eventId)
            .map { eventDetailsDataConverter.convertWithId(it, eventId) }
    }

    override fun complainEvent(
        token: String,
        eventId: String,
        body: EventComplaintRequestBody
    ): Completable {
        return eventsApiMapper.complainEvent(token, eventId, body)
    }

    override fun participateInEvent(
        token: String,
        eventId: String
    ): Completable {
        return eventsApiMapper.participateInEvent(token, eventId)
    }

    override fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventDetailsDomainModel>> {
        return eventsApiMapper.searchEventsByInterests(token, interests)
            .map { eventsDataConverter.convert(it) }
    }

    override fun searchEventsByTextQuery(
        token: String,
        text: String
    ): Single<List<EventDetailsDomainModel>> {
        return eventsApiMapper.searchEventsByTextQuery(token, text)
            .map { eventsDataConverter.convert(it) }
    }
}