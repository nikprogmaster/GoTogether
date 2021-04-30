package com.kandyba.gotogether.data.converter.users

import com.kandyba.gotogether.models.data.events.Participant
import com.kandyba.gotogether.models.general.BaseConverter

class ParticipantsConverter :
    BaseConverter<List<Participant>, List<com.kandyba.gotogether.models.domain.events.Participant>>() {

    override fun convert(from: List<Participant>): List<com.kandyba.gotogether.models.domain.events.Participant> {
        return from.map {
            com.kandyba.gotogether.models.domain.events.Participant(
                it.id,
                it.firstName,
                it.avatar
            )
        }
    }
}