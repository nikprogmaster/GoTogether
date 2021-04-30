package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.data.repository.AuthRepository
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.requests.LoginRequestBody
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single

class AuthInteractorImpl(
    private val repository: AuthRepository
): AuthInteractor {

    override fun signup(credential: SignupRequestBody): Single<SignupDomainResponse> {
        return repository.signup(credential)
    }

    override fun login(credential: LoginRequestBody): Single<LoginDomainResponse> {
        return repository.login(credential)
    }

    override fun logout(token: String): Completable {
        return repository.logout(token)
    }


}