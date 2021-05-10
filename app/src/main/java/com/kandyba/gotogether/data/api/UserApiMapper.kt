package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.events.Participant
import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApiMapper {

    @POST(USER_INFO_ENDPOINT)
    fun updateMainUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body bodyMain: UserMainRequestBody
    ): Single<UserInfoDataModel>

    @POST(USER_INFO_ENDPOINT)
    fun updateUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: UserInfoRequestBody
    ): Single<UserInfoDataModel>

    @POST(USER_INFO_ENDPOINT)
    fun updateUserInterests(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: Map<String, Int>
    ): Single<UserInfoDataModel>

    @GET("$USER_INFO_ENDPOINT/{${UUID_VALUE}}")
    fun getUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(UUID_VALUE) userId: String
    ): Single<UserInfoDataModel>

    @Multipart
    @POST(USER_AVATAR_ENDPOINT)
    fun uploadUserAvatar(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Part filePart: MultipartBody.Part/*,
        @Part("type") type: RequestBody*/
    ): Completable

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