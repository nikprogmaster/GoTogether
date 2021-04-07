package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    val shortTitle: String,

    @SerializedName("slug")
    @Expose
    val slug: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("bodyText")
    @Expose
    val bodyText: String,

    @SerializedName("kudagoUrl")
    @Expose
    val kudagoUrl: String? = null,

    @SerializedName("placeId")
    @Expose
    val placeId: String? = null,

    @SerializedName("latitude")
    @Expose
    val latitude: String,

    @SerializedName("longitude")
    @Expose
    val longitude: String,

    @SerializedName("language")
    @Expose
    val language: String,

    @SerializedName("ageRestriction")
    @Expose
    val ageRestriction: String,

    @SerializedName("isFree")
    @Expose
    val isFree: Boolean,

    @SerializedName("price")
    @Expose
    val price: String,

    @SerializedName("images")
    @Expose
    val images: List<String>,

    @SerializedName("dates")
    @Expose
    val dates: List<DateDataModel>,

    @SerializedName("categories")
    @Expose
    val categories: List<String>,

    @SerializedName("participants")
    @Expose
    val participants: List<Participant>,

    @SerializedName("amountOfParticipants")
    @Expose
    val amountOfParticipants: Int
)

data class Participant(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("firstName")
    @Expose
    val firstName: String,

    @SerializedName("avatar")
    @Expose
    val avatar: String
)
