package com.kandyba.gotogether.models.domain.messages

/**
 * Модель сообщения (domain-слой)
 */
data class MessageDomainModel(
    val userId: String,
    val dialogId: String,
    val text: String,
    val createdAt: Long
)