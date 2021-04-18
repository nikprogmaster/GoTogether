package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.general.UserInfoRequestBody
import com.kandyba.gotogether.models.general.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.UserMainRequestBody
import io.reactivex.Single
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
        @Body body: UserInterestsRequestBody
    ): Single<UserInfoDataModel>

    @GET("$USER_INFO_ENDPOINT/{${UUID_VALUE}}")
    fun getUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(UUID_VALUE) userId: String
    ): Single<UserInfoDataModel>

    companion object {
        private const val USER_INFO_ENDPOINT = "users"
        private const val UUID_VALUE = "id"
        private const val AUTHORIZATION_VALUE = "ssid"
    }
}