package com.kandyba.gotogether.data.converter.events

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.domain.events.Date
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.domain.events.Participant
import com.kandyba.gotogether.models.general.BaseConverter

class EventDetailsDataConverter : BaseConverter<EventDetailsDataModel, EventDetailsDomainModel>() {

    override fun convert(from: EventDetailsDataModel): EventDetailsDomainModel {
        return EventDetailsDomainModel(
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
            from.createdAt,
            from.updatedAt,
            from.dates.map { Date(it.startUnix, it.endUnix) },
            from.categories,
            from.participants.map { Participant(it.id, it.firstName, it.avatar) },
            from.amountOfParticipants
        )
    }
}