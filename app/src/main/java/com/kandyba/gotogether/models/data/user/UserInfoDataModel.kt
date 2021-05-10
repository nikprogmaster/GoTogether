package com.kandyba.gotogether.models.data.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kandyba.gotogether.models.data.events.EventDetailsDataModel


data class UserInfoDataModel(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null,

    @SerializedName("birthDate")
    @Expose
    val birthDate: String? = null,

    @SerializedName("sex")
    @Expose
    val sex: String? = null,

    @SerializedName("longitude")
    @Expose
    val longitude: String? = null,

    @SerializedName("latitude")
    @Expose
    val latitude: String? = null,

    @SerializedName("info")
    @Expose
    val info: String? = null,

    @SerializedName("isLoyal")
    @Expose
    val isLoyal: Boolean,

    @SerializedName("avatar")
    @Expose
    val avatar: String? = null,

    @SerializedName("interests")
    @Expose
    val interests: List<Map<String, Int>>,

    @SerializedName("likedEvents")
    @Expose
    val likedEvents: List<EventDetailsDataModel>,

    @SerializedName("currentUserInBlackList")
    @Expose
    val currentUserInBlackList: Boolean? = null,

    @SerializedName("inCurrentUserBlackList")
    @Expose
    val inCurrentUserBlackList: Boolean? = null
)