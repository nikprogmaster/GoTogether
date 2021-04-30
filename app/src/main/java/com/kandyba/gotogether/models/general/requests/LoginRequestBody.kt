package com.kandyba.gotogether.models.general.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password")
    @Expose
    val password: String
)