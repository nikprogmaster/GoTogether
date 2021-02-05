package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.data.repository.AuthRepository
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.LoginRequestBody
import com.kandyba.gotogether.models.general.SignupRequestBody
import com.kandyba.gotogether.models.presentation.AuthResponse
import io.reactivex.Completable
import io.reactivex.Single

class AuthInteractorImpl(
    private val repository: AuthRepository,
    private val loginConverter: LoginDomainResponseConverter,
    private val signupConverter: SignupDomainResponseConverter
): AuthInteractor {

    override fun signup(credential: SignupRequestBody): Single<AuthResponse> {
        return repository.signup(credential).map { signupConverter.convert(it) }
    }

    override fun login(credential: LoginRequestBody): Single<AuthResponse> {
        return repository.login(credential). map { loginConverter.convert(it) }
    }

    override fun logout(token: String): Completable {
        return repository.logout(token)
    }


}