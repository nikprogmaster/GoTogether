package com.kandyba.gotogether.models.presentation

import com.kandyba.gotogether.models.general.SocketMessage

/**
 * Модель сообщения (presentation-слой)
 */
data class Message(
    val userId: String,
    val dialogId: String,
    val text: String,
    val createdAt: Long,
    val isMyMessage: Boolean,
    var delivered: Boolean
) {

    companion object {
        /**
         * Конвертировать сообщения из сущности полученную с бэка
         * в сущность presentation-слоя приложения
         *
         * @param socketMessage сообщение, полученное с бэка
         * @param userId id пользователя
         * @return [Message] сконвертированное сообщенме
         */
        fun convertFromSocketMessage(socketMessage: SocketMessage, userId: String): Message {
            return Message(
                socketMessage.userId,
                socketMessage.dialogId,
                socketMessage.message,
                socketMessage.time ?: 0,
                socketMessage.userId == userId,
                true
            )
        }
    }

}