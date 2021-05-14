package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель детальной информации о мероприятии (data-слой)
 */
data class EventDetailsDataModel(
    @SerializedName("likedByUser")
    @Expose
    val likedByUser: Boolean,

    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("shortTitle")
    @Expose
    val shortTitle: String? = null,

    @SerializedName("slug")
    @Expose
    val slug: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("bodyText")
    @Expose
    val bodyText: String? = null,

    @SerializedName("kudagoUrl")
    @Expose
    val kudagoUrl: String? = null,

    @SerializedName("place")
    @Expose
    val place: Place? = null,

    @SerializedName("latitude")
    @Expose
    val latitude: String? = null,

    @SerializedName("longitude")
    @Expose
    val longitude: String? = null,

    @SerializedName("language")
    @Expose
    val language: String? = null,

    @SerializedName("ageRestriction")
    @Expose
    val ageRestriction: String? = null,

    @SerializedName("isFree")
    @Expose
    val isFree: Boolean? = null,

    @SerializedName("price")
    @Expose
    val price: String? = null,

    @SerializedName("images")
    @Expose
    val images: List<String>,

    @SerializedName("dates")
    @Expose
    val dates: List<DateDataModel>? = null,

    @SerializedName("categories")
    @Expose
    val categories: List<String>? = null,

    @SerializedName("participants")
    @Expose
    val participants: List<Participant>? = null,

    @SerializedName("amountOfParticipants")
    @Expose
    val amountOfParticipants: Int? = null
)

/**
 * Модель участника мероприятия (data-слой)
 */
data class Participant(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
)

/**
 * Модель места проведения мероприятия (data-слой)
 */
data class Place(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("slug")
    @Expose
    val slug: String? = null,

    @SerializedName("address")
    @Expose
    val address: String? = null,

    @SerializedName("siteUrl")
    @Expose
    val siteUrl: String? = null,

    @SerializedName("timetable")
    @Expose
    val timetable: String? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null,

    @SerializedName("bodyText")
    @Expose
    val bodyText: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("foreignUrl")
    @Expose
    val foreignUrl: String? = null,

    @SerializedName("longitude")
    @Expose
    val longitude: String? = null,

    @SerializedName("latitude")
    @Expose
    val latitude: String? = null,

    @SerializedName("subway")
    @Expose
    val subway: String? = null
)
