package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.LoginRequestBody
import com.kandyba.gotogether.models.general.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signup(credential: SignupRequestBody): Single<SignupDomainResponse>

    fun login(credential: LoginRequestBody): Single<LoginDomainResponse>

    fun logout(token: String): Completable
}