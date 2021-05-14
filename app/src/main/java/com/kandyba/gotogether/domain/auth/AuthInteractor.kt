package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.requests.LoginRequestBody
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Интерактор для авторизации пользователя
 */
interface AuthInteractor {

    /**
     * Зарегистрироваться в приложении
     *
     * @param credential данные регистрации (логин, пароль)
     * @return [Single] ответ с id пользователя
     */
    fun signup(credential: SignupRequestBody): Single<SignupDomainResponse>

    /**
     * Войти в приложение (открыть сессию)
     *
     * @param credential данные авторизации (логин, пароль)
     * @return [Single] ответ с id пользователя и токеном сессии
     */
    fun login(credential: LoginRequestBody): Single<LoginDomainResponse>

    /**
     * Выйти из приложения (завершить сессию)
     *
     * @param token токен сессии пользователя
     * @return [Completable]
     */
    fun logout(token: String): Completable
}