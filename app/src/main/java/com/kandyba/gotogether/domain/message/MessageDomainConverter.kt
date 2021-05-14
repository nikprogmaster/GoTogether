package com.kandyba.gotogether.domain.message

import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.presentation.Message

/**
 * Конвертер сообщений из domain слоя в presentation
 */
class MessageDomainConverter {

    fun convert(from: List<MessageDomainModel>, userId: String): List<Message> {
        return from.map {
            Message(
                it.userId,
                it.dialogId,
                it.text,
                it.createdAt,
                it.userId == userId,
                true
            )
        }
    }

}