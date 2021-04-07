package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.Date
import com.kandyba.gotogether.models.presentation.EventModel

class EventsDomainConverter :
    BaseConverter<List<EventDetailsDomainModel>, List<EventModel>>() {

    override fun convert(from: List<EventDetailsDomainModel>): List<EventModel> {
        return from.map {
            EventModel(
                it.id,
                it.title,
                it.images,
                it.likedByUser,
                it.dates?.map { date -> Date(date.startUnix, date.endUnix) },
                it.price,
                it.isFree,
                it.categories,
                true
            )
        }
    }

}