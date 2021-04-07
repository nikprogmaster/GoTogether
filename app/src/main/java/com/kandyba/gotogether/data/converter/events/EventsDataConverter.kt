package com.kandyba.gotogether.data.converter.events

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.domain.events.DateDomainModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.domain.events.Participant
import com.kandyba.gotogether.models.general.BaseConverter

class EventsDataConverter :
    BaseConverter<List<EventDetailsDataModel>, List<EventDetailsDomainModel>>() {

    override fun convert(from: List<EventDetailsDataModel>): List<EventDetailsDomainModel> {
        return from.map {
            EventDetailsDomainModel(
                it.id,
                it.likedByUser,
                it.title,
                it.shortTitle,
                it.slug,
                it.description,
                it.bodyText,
                it.kudagoUrl,
                it.placeId,
                it.latitude,
                it.longitude,
                it.language,
                it.ageRestriction,
                it.isFree,
                it.price,
                it.images,
                it.dates?.map { date -> DateDomainModel(date.startUnix, date.endUnix) }
                    ?: emptyList(),
                it.categories,
                it.participants?.map { part -> Participant(part.id, part.firstName, part.avatar) }
                    ?: emptyList(),
                it.amountOfParticipants
            )
        }
    }

}