package com.kandyba.gotogether.models.data.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginDataResponse(
    @SerializedName("id")
    @Expose
    var userId: String,
    @SerializedName("ssid")
    @Expose
    var token: String
)

