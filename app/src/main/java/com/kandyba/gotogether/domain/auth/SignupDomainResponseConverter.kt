package com.kandyba.gotogether.domain.auth

import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.Date
import com.kandyba.gotogether.models.presentation.EventModel

class SignupDomainResponseConverter: BaseConverter<SignupDomainResponse, AuthResponse>() {

    override fun convert(from: SignupDomainResponse): AuthResponse {
        return AuthResponse(
            from.id,
            false,
            from.token,
            from.events.mapValues { pair ->
                EventModel(
                    pair.key,
                    pair.value.title,
                    pair.value.photoLinks,
                    pair.value.likedByUser,
                    pair.value.dates.map { date -> Date(date.startUnix, date.endUnix) },
                    pair.value.price,
                    pair.value.isFree,
                    pair.value.categories
                )
            }
        )
    }
}