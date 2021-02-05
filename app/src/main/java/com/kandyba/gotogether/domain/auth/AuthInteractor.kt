package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.models.data.auth.LoginDataResponse
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.general.LoginRequestBody
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.SignupRequestBody
import com.kandyba.gotogether.models.presentation.AuthResponse
import io.reactivex.Completable
import io.reactivex.Single

interface AuthInteractor {

    fun signup(credential: SignupRequestBody): Single<AuthResponse>

    fun login(credential: LoginRequestBody): Single<AuthResponse>

    fun logout(token: String): Completable
}