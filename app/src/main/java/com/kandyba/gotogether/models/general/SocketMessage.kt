package com.kandyba.gotogether.models.general

/**
 * Модель сокета, использующаяся для отправки и получения сообщения
 */
data class SocketMessage(
    val id: String? = null,
    val hash: String? = null,
    val userId: String,
    val dialogId: String,
    val message: String,
    val time: Long? = null,
    val delivered: Boolean? = null
)