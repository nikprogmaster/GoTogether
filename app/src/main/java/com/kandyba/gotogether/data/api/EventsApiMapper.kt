package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Контракт маппера событий
 */
interface EventsApiMapper {

    /**
     * Получить рекомендованные события
     *
     * @param token токен сессии пользователя
     * @param amount количество событий
     * @return [Single] c детальной информацией о событии
     */
    @GET("${EVENT_RECOMMENDATIONS_ENDPOINT}{${EVENT_AMOUNT_VALUE}}")
    fun getEventsRecommendations(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_AMOUNT_VALUE) amount: Int
    ): Single<List<EventDetailsDataModel>>

    /**
     * Получить информацию о мероприятиии
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     * @return [Single] c детальной информацией о событии
     */
    @GET("${EVENT_INFO_ENDPOINT}{${EVENT_VALUE}}")
    fun getEventInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_VALUE) eventId: String
    ): Single<EventDetailsDataModel>

    /**
     * Пожаловаться на событие
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     * @param body текст жалобы
     * @return [Completable]
     */
    @POST("$EVENT_COMPLAIN_ENDPOINT{${EVENT_VALUE}}")
    fun complainEvent(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_VALUE) eventId: String,
        @Body body: EventComplaintRequestBody
    ): Completable

    /**
     * Поучаствовать/ отменить участие в событии
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     * @return [Completable]
     */
    @POST("${PARTICIPATION_TO_EVENT_ENDPOINT}{${EVENT_VALUE}}")
    fun participateInEvent(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(EVENT_VALUE) eventId: String
    ): Completable

    /**
     * Поиск мероприятий по категорям
     *
     * @param token токен сессии пользователя
     * @param interests категории
     * @return [Single] список мероприятий
     */
    @GET(EVENT_SEARCH_INTERESTS_ENDPOINT)
    fun searchEventsByInterests(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Query(EVENT_INTERESTS_QUERY) interests: List<String>
    ): Single<List<EventDetailsDataModel>>

    /**
     * Поиск мероприятий по тексту
     *
     * @param token токен сессии пользователя
     * @param text текст запроса
     * @return [Single] список мероприятий
     */
    @GET(EVENT_SEARCH_FULLTEXT_ENDPOINT)
    fun searchEventsByTextQuery(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Query(EVENT_TEXT_QUERY) text: String
    ): Single<List<EventDetailsDataModel>>

    companion object {
        private const val EVENT_RECOMMENDATIONS_ENDPOINT = "recommendations/events/"
        private const val EVENT_INFO_ENDPOINT = "events/"
        private const val EVENT_COMPLAIN_ENDPOINT = "events/complaint/"
        private const val EVENT_SEARCH_INTERESTS_ENDPOINT = "events/search/interests"
        private const val EVENT_SEARCH_FULLTEXT_ENDPOINT = "events/search/fulltext/"
        private const val PARTICIPATION_TO_EVENT_ENDPOINT = "events/participants/"
        private const val EVENT_VALUE = "event_id"
        private const val EVENT_AMOUNT_VALUE = "amount"
        private const val EVENT_INTERESTS_QUERY = "interests"
        private const val EVENT_TEXT_QUERY = "query"
        private const val AUTHORIZATION_VALUE = "ssid"
    }
}