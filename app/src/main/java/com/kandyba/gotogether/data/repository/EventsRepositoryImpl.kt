package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.converter.events.EventComplainDateConverter
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.models.domain.events.EventComplainDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import io.reactivex.Completable
import io.reactivex.Single

class EventsRepositoryImpl(
    private val eventsApiMapper: EventsApiMapper,
    private val eventsDataConverter: EventsDataConverter,
    private val eventDetailsDataConverter: EventDetailsDataConverter,
    private val eventComplainDateConverter: EventComplainDateConverter
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

    override fun eventComplain(
        token: String,
        body: EventComplaintRequestBody
    ): Single<EventComplainDomainModel> {
        return eventsApiMapper.eventComplain(token, body)
            .map { eventComplainDateConverter.convert(it) }
    }

    override fun participateInEvent(
        token: String,
        body: ParticipationRequestBody,
        eventId: String
    ): Completable {
        return eventsApiMapper.participateInEvent(token, body, eventId)
    }
}