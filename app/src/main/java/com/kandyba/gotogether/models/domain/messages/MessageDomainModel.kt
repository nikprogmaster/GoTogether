package com.kandyba.gotogether.models.domain.messages

class MessageDomainModel(
    val userId: String,
    val dialogId: String,
    val text: String,
    val createdAt: Long
)