package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.general.UserRequestBody
import io.reactivex.Single
import retrofit2.http.*

interface UserApiMapper {

    @POST(USER_INFO_ENDPOINT)
    fun updateUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Body body: UserRequestBody
    ): Single<UserInfoDataModel>

    @GET("$USER_INFO_ENDPOINT/{${UUID_VALUE}}/")
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