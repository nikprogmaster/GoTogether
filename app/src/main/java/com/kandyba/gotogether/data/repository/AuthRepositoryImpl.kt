package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.AuthApiMapper
import com.kandyba.gotogether.data.converter.auth.LoginDataResponseConverter
import com.kandyba.gotogether.data.converter.auth.SignupDataResponseConverter
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.requests.LoginRequestBody
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepositoryImpl(
    private val authApiMapper: AuthApiMapper,
    private val loginDataResponseConverter: LoginDataResponseConverter,
    private val signupDataResponseConverter: SignupDataResponseConverter
) : AuthRepository {

    override fun signup(credential: SignupRequestBody): Single<SignupDomainResponse> {
        return authApiMapper.signup(credential)
            .map { response -> signupDataResponseConverter.convert(response) }
    }

    override fun login(credential: LoginRequestBody): Single<LoginDomainResponse> {
        return authApiMapper.login(credential)
            .map { response -> loginDataResponseConverter.convert(response) }
    }

    override fun logout(token: String): Completable {
        return authApiMapper.logout(token)
    }
}