package com.kandyba.gotogether.models.domain.events

import java.io.Serializable

class EventDetailsDomainModel(
    val id: String,
    val likedByUser: Boolean,
    val title: String,
    val shortTitle: String,
    val slug: String,
    val description: String,
    val bodyText: String,
    val kudagoUrl: String? = null,
    val placeId: String? = null,
    val latitude: String,
    val longitude: String,
    val language: String,
    val ageRestriction: String,
    val isFree: Boolean,
    val price: String,
    val images: List<String>,
    val dates: List<DateDomainModel>,
    val categories: List<String>,
    val participants: List<Participant>,
    val amountOfParticipants: Int
) : Serializable

class Participant(
    val id: String,
    val firstName: String,
    val avatar: String
) : Serializable

class ParticipantsList(
    val participants: List<Participant>
) : Serializable