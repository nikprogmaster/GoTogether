package com.kandyba.gotogether.models.data.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kandyba.gotogether.models.data.events.EventInfoDataModel


data class UserInfoDataModel(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("first_name")
    @Expose
    val firstName: String,

    @SerializedName("birth_date")
    @Expose
    val birthDate: String,

    @SerializedName("sex")
    @Expose
    val sex: String,

    @SerializedName("age")
    @Expose
    val age: String,

    @SerializedName("longitude")
    @Expose
    val longitude: String,

    @SerializedName("latitude")
    @Expose
    val latitude: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String,

    @SerializedName("events")
    @Expose
    val events: Map<String, EventInfoDataModel>
)