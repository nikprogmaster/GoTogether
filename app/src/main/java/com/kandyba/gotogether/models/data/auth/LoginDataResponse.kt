package com.kandyba.gotogether.models.data.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kandyba.gotogether.models.data.events.EventInfoDataModel

class LoginDataResponse (
    @SerializedName("user")
    @Expose
    var user: String,

    @SerializedName("is_loyal")
    @Expose
    var isLoyal: Boolean,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("events")
    @Expose
    var events: Map<String, EventInfoDataModel>
)

