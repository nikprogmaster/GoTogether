package com.kandyba.gotogether.domain.events

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.kandyba.gotogether.R
import com.kandyba.gotogether.data.repository.EventsRepository
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class EventsInteractorImpl(
    private val eventsRepository: EventsRepository,
    private val eventsDomainConverter: EventsDomainConverter
): EventsInteractor {

    override fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>> {
        return if (Cache.instance.getCachedEvents() == null) {
            eventsRepository.getEventsRecommendations(token, amount)
                .map { eventsDomainConverter.convert(it) }
                .doAfterSuccess { Cache.instance.saveEvents(it) }
        } else {
            Single.just(Cache.instance.getCachedEvents())
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

    override fun participateInEvent(token: String, eventId: String): Completable {
        return eventsRepository.participateInEvent(token, eventId)
            .doOnComplete { Cache.instance.clearUserCache() }
    }

    override fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventModel>> {
        return eventsRepository.searchEventsByInterests(token, interests)
            .map { eventsDomainConverter.convert(it) }
    }

    override fun searchEventsByTextQuery(token: String, text: String): Single<List<EventModel>> {
        return eventsRepository.searchEventsByTextQuery(token, text)
            .map { eventsDomainConverter.convert(it) }
    }

    override fun loadEventImages(
        urls: List<String>,
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int
    ): Single<List<Bitmap>> {
        return Observable.fromIterable(urls)
            .flatMap { eventsRepository.loadImage(it, placeholder, error) }
            .toList()
    }

    private fun loadImage(url: String): Observable<Bitmap> {
        return Observable.just(
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ill_placeholder_300dp)
                .error(R.drawable.ill_error_placeholder_300dp)
                .get()
        )
    }
}