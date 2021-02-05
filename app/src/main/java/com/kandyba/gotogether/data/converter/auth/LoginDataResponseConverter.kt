package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.auth.LoginDataResponse
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.domain.events.Date
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class LoginDataResponseConverter: BaseConverter<LoginDataResponse, LoginDomainResponse>() {

    override fun convert(from: LoginDataResponse): LoginDomainResponse {
        return LoginDomainResponse(
            from.user,
            from.isLoyal,
            from.token,
            from.events.mapValues { pair -> EventInfoDomainModel(
                pair.value.title,
                pair.value.photoLinks,
                pair.value.likedByUser,
                pair.value.dates.map { date -> Date(date.startUnix, date.endUnix) },
                pair.value.price,
                pair.value.isFree,
                pair.value.categories
            ) }
        )
    }
}