package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class EventComplainDataModel(

    @SerializedName("event_id")
    @Expose
    val eventId: String,

    @SerializedName("user_id")
    @Expose
    val userId: String,

    @SerializedName("text")
    @Expose
    val text: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String

)