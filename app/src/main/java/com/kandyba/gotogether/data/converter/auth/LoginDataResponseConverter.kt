package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.auth.LoginDataResponse
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.general.BaseConverter

/**
 * Конвертер для LoginData из data слоя в domain
 */
class LoginDataResponseConverter: BaseConverter<LoginDataResponse, LoginDomainResponse>() {

    override fun convert(from: LoginDataResponse): LoginDomainResponse {
        return LoginDomainResponse(
            from.userId,
            from.token
        )
    }
}