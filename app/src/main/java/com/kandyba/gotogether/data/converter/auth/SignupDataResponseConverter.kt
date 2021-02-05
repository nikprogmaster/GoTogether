package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.auth.SignupDataResponse

import com.kandyba.gotogether.models.domain.auth.SignupDomainResponse
import com.kandyba.gotogether.models.domain.events.Date
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class SignupDataResponseConverter: BaseConverter<SignupDataResponse, SignupDomainResponse>() {

    override fun convert(from: SignupDataResponse): SignupDomainResponse {
        return SignupDomainResponse(
            from.id,
            from.token,
            from.events?.mapValues { pair -> EventInfoDomainModel(
                pair.value.title,
                pair.value.photoLinks,
                pair.value.likedByUser,
                pair.value.dates?.map { date -> Date(date.startUnix, date.endUnix) },
                pair.value.price,
                pair.value.isFree,
                pair.value.categories
            ) }
        )
    }
}