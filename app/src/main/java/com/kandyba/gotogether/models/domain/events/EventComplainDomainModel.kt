package com.kandyba.gotogether.models.domain.events

class EventComplainDomainModel(
    val eventId: String,
    val userId: String,
    val text: String,
    val createdAt: String,
    val updatedAt: String
)