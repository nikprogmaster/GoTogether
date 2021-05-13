package com.kandyba.gotogether.models.presentation

import com.kandyba.gotogether.models.general.SocketMessage

/**
 * @author Кандыба Никита
 * @since 30.04.2021
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