package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.auth.SignupDataResponse
import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.BaseConverter

class SignupDataResponseConverter: BaseConverter<SignupDataResponse, SignupDomainResponse>() {

    override fun convert(from: SignupDataResponse): SignupDomainResponse {
        return SignupDomainResponse(from.id)
    }
}