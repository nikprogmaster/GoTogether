package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.data.auth.LoginDataResponse
import com.kandyba.gotogether.models.data.auth.SignupDataResponse
import com.kandyba.gotogether.models.general.requests.LoginRequestBody
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiMapper {

    @POST(SIGNUP_ENDPOINT)
    fun signup(@Body body: SignupRequestBody): Single<SignupDataResponse>

    @POST(LOGIN_ENDPOINT)
    fun login(@Body body: LoginRequestBody): Single<LoginDataResponse>

    @DELETE(LOGOUT_ENDPOINT)
    fun logout(@Header(AUTHORIZATION_VALUE) token: String): Completable

    companion object Endpoints {
        private const val SIGNUP_ENDPOINT = "signup"
        private const val LOGIN_ENDPOINT = "login"
        private const val LOGOUT_ENDPOINT = "logout"
        private const val AUTHORIZATION_VALUE = "ssid"
    }
}

