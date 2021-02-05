package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.events.EventInfoDataModel
import com.kandyba.gotogether.models.domain.events.Date
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class EventsDataConverter: BaseConverter<Map<String, EventInfoDataModel>, Map<String, EventInfoDomainModel>>() {

    override fun convert(from: Map<String, EventInfoDataModel>): Map<String, EventInfoDomainModel> {
        return from.mapValues { pair -> EventInfoDomainModel(
            pair.value.title,
            pair.value.photoLinks,
            pair.value.likedByUser,
            pair.value.dates.map { date -> Date(date.startUnix, date.endUnix) },
            pair.value.price,
            pair.value.isFree,
            pair.value.categories
        ) }
    }

}