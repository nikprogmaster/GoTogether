package com.kandyba.gotogether.domain.events

import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.Date
import com.kandyba.gotogether.models.presentation.EventModel

class EventsDomainConverter :
    BaseConverter<Map<String, EventInfoDomainModel>, Map<String, EventModel>>() {

    override fun convert(from: Map<String, EventInfoDomainModel>): Map<String, EventModel> {
        return from.mapValues { pair ->
            EventModel(
                pair.value.title,
                pair.value.photoLinks,
                pair.value.likedByUser,
                pair.value.dates.map { date -> Date(date.startUnix, date.endUnix) },
                pair.value.price,
                pair.value.isFree,
                pair.value.categories
            )
        }
    }

}