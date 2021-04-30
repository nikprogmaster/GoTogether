package com.kandyba.gotogether.domain.message

import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.presentation.Message


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