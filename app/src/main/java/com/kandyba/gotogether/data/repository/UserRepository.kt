package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

/**
 * Репозиторий пользователей
 */
interface UserRepository {

    /**
     * Получить информацию о пользователе
     *
     * @param token токен сессии пользователя
     * @param userId id пользователя
     * @return [Single] информация о пользователе
     */
    fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel>

    /**
     * Обновить главную информацию пользователя (имя, дата рождения, пол)
     *
     * @param token токен сессии пользователя
     * @param bodyMain новые значения полей
     * @return [Single] информация о пользователе
     */
    fun updateMainUserInfo(
        token: String,
        bodyMain: UserMainRequestBody
    ): Single<UserInfoDomainModel>

    /**
     * Обновить информацию блока "обо мне"
     *
     * @param token токен сессии пользователя
     * @param body новая информация
     * @return [Single] информация о пользователе
     */
    fun updateUserInfo(token: String, body: UserInfoRequestBody): Single<UserInfoDomainModel>

    /**
     * Обновить интересы пользователя
     *
     * @param token токен авторизации
     * @param body мапа с данными "название интереса" : "уровень" (от 0 до 100)
     * @return [Single] информация о пользователе
     */
    fun updateUserInterests(
        token: String,
        body: UserInterestsRequestBody
    ): Single<UserInfoDomainModel>

    /**
     * Загрузить аватар пользователя
     *
     * @param token токен сессии пользователя
     * @param filePart изображение, распределенное по частям
     * @return [Completable]
     */
    fun uploadUserAvatar(token: String, filePart: MultipartBody.Part): Completable

    /**
     * Получить рекомендации людей
     *
     * @param token токен сессии пользователя
     * @param amount количество рекомендаций
     * @return [Single] список участников
     */
    fun getParticipantsRecommendations(
        token: String,
        amount: Int
    ): Single<List<com.kandyba.gotogether.models.domain.events.Participant>>
}


