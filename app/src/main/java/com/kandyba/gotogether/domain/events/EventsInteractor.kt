package com.kandyba.gotogether.domain.events

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Интерактор для событий
 */
interface EventsInteractor {

    /**
     * Получить рекомендованные события
     *
     * @param token токен сессии пользователя
     * @param amount количество рекомендаций
     * @return [Single] список рекомендованных событий
     */
    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventModel>>

    /**
     * Получить информацию о событии
     *
     * @param token токен сессии пользователя
     * @param eventId id события
     * @return [Single] детальная информация о событии
     */
    fun getEventInfo(token: String, eventId: String): Single<EventDetailsDomainModel>

    /**
     * Пожаловаться на событие
     *
     * @param token токен сессии пользователя
     * @param eventId id события
     * @param body текст жалобы
     * @return [Completable]
     */
    fun complainEvent(token: String, eventId: String, body: EventComplaintRequestBody): Completable

    /**
     * Поучаствовать/ отменить участие в событии
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     * @return [Completable]
     */
    fun participateInEvent(token: String, eventId: String): Completable

    /**
     * Поиск мероприятий по категорям
     *
     * @param token токен сессии пользователя
     * @param interests категории
     * @return [Single] список мероприятий
     */
    fun searchEventsByInterests(
        token: String,
        interests: List<String>
    ): Single<List<EventModel>>

    /**
     * Поиск мероприятий по тексту
     *
     * @param token токен сессии пользователя
     * @param text текст запроса
     * @return [Single] список мероприятий
     */
    fun searchEventsByTextQuery(token: String, text: String): Single<List<EventModel>>

    /**
     * Загрузить список картинок с заданного URL
     *
     * @param urls список адресов картинок
     * @param placeholder заменяющий изображение на время загрузки
     * @param error изображение в случае ошибки при загрузки оригинального изображения
     * @return [Single] список загруженных картинок
     */
    fun loadEventImages(
        urls: List<String>,
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int
    ): Single<List<Bitmap>>

}