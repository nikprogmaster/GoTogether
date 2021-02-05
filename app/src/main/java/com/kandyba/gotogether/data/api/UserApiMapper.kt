package com.kandyba.gotogether.data.api

import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiMapper {

    @PUT
    fun updateUserInfo(@Header(AUTHORIZATION_VALUE) token: String, @Query(UUID_VALUE) uid: Int)

    companion object {
        private const val USER_INFO_ENDPOINT = "users/"
        private const val UUID_VALUE = "user_uuid"
        private const val AUTHORIZATION_VALUE = "user_uuid"
    }
}