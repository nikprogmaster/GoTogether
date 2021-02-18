package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiMapper {

    @PUT("$USER_INFO_ENDPOINT{$UUID_VALUE}/")
    fun updateUserInfo(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(UUID_VALUE) uid: String,
        @Body body: Map<String, String>? = null
    ): Single<UserInfoDataModel>

    companion object {
        private const val USER_INFO_ENDPOINT = "users/"
        private const val UUID_VALUE = "user_uuid"
        private const val AUTHORIZATION_VALUE = "Authorization"
    }
}