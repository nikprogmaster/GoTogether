package com.kandyba.gotogether.data.converter.messages

import com.kandyba.gotogether.models.data.messages.MessageDataModel
import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

/**
 * Конвертер списка сообщений из data слоя в domain
 */
class MessageDataConverter : BaseConverter<List<MessageDataModel>, List<MessageDomainModel>>() {

    override fun convert(from: List<MessageDataModel>): List<MessageDomainModel> {
        return from.map {
            MessageDomainModel(
                it.userId,
                it.dialogId,
                it.text,
                it.createdAt
            )
        }
    }

}