package com.kandyba.gotogether.data.converter.events

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.domain.events.DateDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.domain.events.Participant

class EventDetailsDataConverter {

    fun convertWithId(from: EventDetailsDataModel, eventId: String): EventDetailsDomainModel {
        return EventDetailsDomainModel(
            eventId,
            from.likedByUser,
            from.title,
            from.shortTitle,
            from.slug,
            from.description,
            from.bodyText,
            from.kudagoUrl,
            from.placeId,
            from.latitude,
            from.longitude,
            from.language,
            from.ageRestriction,
            from.isFree,
            from.price,
            from.images,
            from.dates?.map { DateDomainModel(it.startUnix, it.endUnix) } ?: emptyList(),
            from.categories,
            from.participants?.map { Participant(it.id, it.firstName, it.avatar) } ?: emptyList(),
            from.amountOfParticipants
        )
    }

}