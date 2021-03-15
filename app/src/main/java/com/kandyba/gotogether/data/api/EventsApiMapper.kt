package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.data.events.EventsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface EventsApiMapper {

    @GET(EVENT_RECOMMENDATIONS_ENDPOINT)
    fun getEventsRecommendations(
        @Header("Authorization") token: String
    ): Single<EventsResponse>

    @GET("${EVENT_INFO_ENDPOINT}${EVENT_VALUE}/")
    fun getEventInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_VALUE) eventId: String
    ): Single<EventDetailsDataModel>

    companion object {
        private const val EVENT_RECOMMENDATIONS_ENDPOINT = "recommendations/events/"
        private const val EVENT_INFO_ENDPOINT = "/api/v1/events/"
        private const val EVENT_VALUE = "event_id"
        private const val AUTHORIZATION_VALUE = "Authorization"
    }
}