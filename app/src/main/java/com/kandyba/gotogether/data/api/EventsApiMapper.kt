package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.EventInfoDataModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface EventsApiMapper {

    @GET(EVENT_RECOMMENDATIONS)
    fun getEventsRecommendations(
        @Header("Authorization") token: String
    ): Single<Map<String, EventInfoDataModel>>

    companion object {
        private const val EVENT_RECOMMENDATIONS = "recommendations/events/"
    }
}