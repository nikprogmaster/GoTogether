package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.requests.LoginRequestBody
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signup(credential: SignupRequestBody): Single<SignupDomainResponse>

    fun login(credential: LoginRequestBody): Single<LoginDomainResponse>

    fun logout(token: String): Completable
}