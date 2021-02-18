package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.converter.auth.EventsDataConverter
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import io.reactivex.Single

class EventsRepositoryImpl(
    private val eventsApiMapper: EventsApiMapper,
    private val eventsDataConverter: EventsDataConverter
): EventsRepository {

    override fun getEventsRecommendations(token: String): Single<Map<String, EventInfoDomainModel>> {
        val tokenResult = TOKEN_PREFIX + token
        return eventsApiMapper.getEventsRecommendations(tokenResult)
            .map { eventsDataConverter.convert(it.events) }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}