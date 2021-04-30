package com.kandyba.gotogether.data.converter.events

import com.kandyba.gotogether.models.data.events.EventDetailsDataModel
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class EventsDataConverter(
    private val eventDetailsDataConverter: EventDetailsDataConverter
) : BaseConverter<List<EventDetailsDataModel>, List<EventDetailsDomainModel>>() {

    override fun convert(from: List<EventDetailsDataModel>): List<EventDetailsDomainModel> {
        return from.map {
            eventDetailsDataConverter.convertWithId(it, it.id)
        }
    }

}