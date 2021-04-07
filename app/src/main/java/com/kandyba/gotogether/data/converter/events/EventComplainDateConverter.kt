package com.kandyba.gotogether.data.converter.events

import com.kandyba.gotogether.models.data.events.EventComplainDataModel
import com.kandyba.gotogether.models.domain.events.EventComplainDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

/**
 * @author Кандыба Никита
 * @since 26.03.2021
 */
class EventComplainDateConverter :
    BaseConverter<EventComplainDataModel, EventComplainDomainModel>() {

    override fun convert(from: EventComplainDataModel): EventComplainDomainModel {
        return EventComplainDomainModel(
            from.eventId,
            from.userId,
            from.text,
            from.createdAt,
            from.updatedAt
        )
    }
}