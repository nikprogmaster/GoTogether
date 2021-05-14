package com.kandyba.gotogether.models.domain.events

import java.io.Serializable

/**
 * Модель детальной информации о мероприятии (domain-слой)
 */
data class EventDetailsDomainModel(
    val id: String,
    val likedByUser: Boolean,
    val title: String,
    val shortTitle: String?,
    val slug: String?,
    val description: String?,
    val bodyText: String?,
    val kudagoUrl: String?,
    val place: Place?,
    val latitude: String?,
    val longitude: String?,
    val language: String?,
    val ageRestriction: String?,
    val isFree: Boolean?,
    val price: String?,
    val images: List<String>,
    val dates: List<DateDomainModel>?,
    val categories: List<String>?,
    val participants: List<Participant>?,
    val amountOfParticipants: Int?
) : Serializable

/**
 * Модель участника мероприятия (domain-слой)
 */
data class Participant(
    val id: String,
    val firstName: String?,
    var avatar: String? = null
) : Serializable

/**
 * Модель места проведения мероприятия (domain-слой)
 */
data class Place(
    val title: String,
    val slug: String? = null,
    val address: String? = null,
    val siteUrl: String? = null,
    var timetable: String? = null,
    val phone: String? = null,
    val bodyText: String? = null,
    val description: String? = null,
    val foreignUrl: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val subway: String? = null
) : Serializable

/**
 * Список участников мероприятия
 */
class ParticipantsList(
    val participants: List<Participant>
) : Serializable