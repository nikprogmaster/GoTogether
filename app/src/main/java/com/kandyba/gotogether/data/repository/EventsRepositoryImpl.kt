package com.kandyba.gotogether.data.repository

import android.graphics.Bitmap
import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Реализация репозитория событий
 *
 * @constructor
 * @property eventsApiMapper маппер событий
 * @property eventsDataConverter конвертер событий из data слоя в domain
 * @property eventDetailsDataConverter конвертер информации о событии из data слоя в domain
 */
class EventsRepositoryImpl(
    private val eventsApiMapper: EventsApiMapper,
    private val eventsDataConverter: EventsDataConverter,
    private val eventDetailsDataConverter: EventDetailsDataConverter
): EventsRepository {

    override fun getEventsRecommendations(
        token: String,
        amount: Int
    ): Single<List<EventDetailsDomainModel>> {
        return eventsApiMapper.getEventsRecommendations(token, amount)
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

    override fun loadImage(url: String, placeholder: Int, error: Int): Observable<Bitmap> {
        return Observable.just(
            Picasso.get()
                .load(url)
                .placeholder(placeholder)
                .error(error)
                .get()
        )
    }
}