package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.Participant
import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Контракт маппера пользователей
 */
interface UserApiMapper {

    /**
     * Обновить главную информацию пользователя (имя, дата рождения, пол)
     *
     * @param token токен сессии пользователя
     * @param bodyMain новые значения полей
     * @return [Single] информация о пользователе
     */
    @POST(USER_INFO_ENDPOINT)
    fun updateMainUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body bodyMain: UserMainRequestBody
    ): Single<UserInfoDataModel>

    /**
     * Обновить информацию блока "обо мне"
     *
     * @param token токен сессии пользователя
     * @param body новая информация
     * @return [Single] информация о пользователе
     */
    @POST(USER_INFO_ENDPOINT)
    fun updateUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: UserInfoRequestBody
    ): Single<UserInfoDataModel>

    /**
     * Обновить интересы пользователя
     *
     * @param token токен авторизации
     * @param body мапа с данными "название интереса" : "уровень" (от 0 до 100)
     * @return [Single] информация о пользователе
     */
    @POST(USER_INFO_ENDPOINT)
    fun updateUserInterests(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: Map<String, Int>
    ): Single<UserInfoDataModel>

    /**
     * Получить информацию о пользователе
     *
     * @param token токен сессии пользователя
     * @param userId id пользователя
     * @return [Single] информация о пользователе
     */
    @GET("$USER_INFO_ENDPOINT/{${UUID_VALUE}}")
    fun getUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(UUID_VALUE) userId: String
    ): Single<UserInfoDataModel>

    /**
     * Загрузить аватар пользователя
     *
     * @param token токен сессии пользователя
     * @param filePart изображение, распределенное по частям
     * @return [Completable]
     */
    @Multipart
    @POST(USER_AVATAR_ENDPOINT)
    fun uploadUserAvatar(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Part filePart: MultipartBody.Part
    ): Completable

    /**
     * Получить рекомендации людей
     *
     * @param token токен сессии пользователя
     * @param amount количество рекомендаций
     * @return [Single] список участников
     */
    @GET("${USER_PARTICIPATIONS_ENDPOINT}{${USER_AMOUNT_VALUE}}")
    fun getParticipantsRecommendations(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(USER_AMOUNT_VALUE) amount: Int
    ): Single<List<Participant>>

    companion object {
        private const val USER_INFO_ENDPOINT = "users"
        private const val USER_AVATAR_ENDPOINT = "users/avatar"
        private const val USER_PARTICIPATIONS_ENDPOINT = "recommendations/users/"
        private const val USER_AMOUNT_VALUE = "amount"
        private const val UUID_VALUE = "id"
        private const val AUTHORIZATION_VALUE = "ssid"
    }
}