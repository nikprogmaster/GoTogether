package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import io.reactivex.Single

interface EventsRepository {

    fun getEventsRecommendations(token: String): Single<Map<String, EventInfoDomainModel>>
}