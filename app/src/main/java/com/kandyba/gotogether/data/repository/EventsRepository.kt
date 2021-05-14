package com.kandyba.gotogether.data.repository

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Репозиторий мероприятий
 */
interface EventsRepository {

    /**
     * Получить рекомендованные события
     *
     * @param token токен сессии пользователя
     * @param amount количество рекомендаций
     * @return [Single] список рекомендованных событий
     */
    fun getEventsRecommendations(token: String, amount: Int): Single<List<EventDetailsDomainModel>>

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
    ): Single<List<EventDetailsDomainModel>>

    /**
     * Поиск мероприятий по тексту
     *
     * @param token токен сессии пользователя
     * @param text текст запроса
     * @return [Single] список мероприятий
     */
    fun searchEventsByTextQuery(token: String, text: String): Single<List<EventDetailsDomainModel>>

    /**
     * Загрузить изображение с заданного URL
     *
     * @param url адрес ресурса
     * @param placeholder заменяющий изображение на время загрузки
     * @param error изображение в случае ошибки при загрузки оригинального изображения
     * @return [Observable] c данными изображения
     */
    fun loadImage(
        url: String,
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int
    ): Observable<Bitmap>
}