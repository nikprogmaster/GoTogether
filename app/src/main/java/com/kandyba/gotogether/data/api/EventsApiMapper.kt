package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.EventComplainDataModel
import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface EventsApiMapper {

    @GET("${EVENT_RECOMMENDATIONS_ENDPOINT}{${EVENT_AMOUNT_VALUE}}")
    fun getEventsRecommendations(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_AMOUNT_VALUE) amount: Int
    ): Single<List<EventDetailsDataModel>>

    @GET("${EVENT_INFO_ENDPOINT}{${EVENT_VALUE}}")
    fun getEventInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_VALUE) eventId: String
    ): Single<EventDetailsDataModel>

    @POST(EVENT_COMPLAIN_ENDPOINT)
    fun eventComplain(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: EventComplaintRequestBody
    ): Single<EventComplainDataModel>

    @PUT("${PARTICIPATION_TO_EVENT_ENDPOINT}{${EVENT_VALUE}}")
    fun participateInEvent(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: ParticipationRequestBody,
        @Path(EVENT_VALUE) eventId: String
    ): Completable

    companion object {
        private const val EVENT_RECOMMENDATIONS_ENDPOINT = "recommendations/events/"
        private const val EVENT_INFO_ENDPOINT = "events/"
        private const val EVENT_COMPLAIN_ENDPOINT = "complaints/event"
        private const val PARTICIPATION_TO_EVENT_ENDPOINT = "participations/"
        private const val EVENT_VALUE = "event_id"
        private const val EVENT_AMOUNT_VALUE = "amount"
        private const val AUTHORIZATION_VALUE = "ssid"
    }
}