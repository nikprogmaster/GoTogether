package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventDetailsDataModel(
    @SerializedName("liked_by_user")
    @Expose
    val likedByUser: Boolean,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("short_title")
    @Expose
    val shortTitle: String,

    @SerializedName("slug")
    @Expose
    val slug: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("body_text")
    @Expose
    val bodyText: String,

    @SerializedName("kudago_url")
    @Expose
    val kudagoUrl: Any,

    @SerializedName("place_id")
    @Expose
    val placeId: Any,

    @SerializedName("latitude")
    @Expose
    val latitude: String,

    @SerializedName("longitude")
    @Expose
    val longitude: String,

    @SerializedName("language")
    @Expose
    val language: String,

    @SerializedName("age_restriction")
    @Expose
    val ageRestriction: String,

    @SerializedName("is_free")
    @Expose
    val isFree: Boolean,

    @SerializedName("price")
    @Expose
    val price: String,

    @SerializedName("images")
    @Expose
    val images: List<String>,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String,

    @SerializedName("dates")
    @Expose
    val dates: List<Date>,

    @SerializedName("categories")
    @Expose
    val categories: List<String>,

    @SerializedName("participants")
    @Expose
    val participants: List<Participant>,

    @SerializedName("amount_of_participants")
    @Expose
    val amountOfParticipants: Int
)

data class Participant(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("first_name")
    @Expose
    val firstName: String,

    @SerializedName("avatar")
    @Expose
    val avatar: String
)
