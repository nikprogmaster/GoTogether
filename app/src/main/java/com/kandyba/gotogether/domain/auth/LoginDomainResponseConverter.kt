package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.Date
import com.kandyba.gotogether.models.presentation.EventModel

class LoginDomainResponseConverter: BaseConverter<LoginDomainResponse, AuthResponse>() {

    override fun convert(from: LoginDomainResponse): AuthResponse {
        return AuthResponse(
            from.user,
            from.isLoyal,
            from.token,
            from.events.mapValues { pair -> EventModel(
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