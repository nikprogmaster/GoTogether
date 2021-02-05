package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignupRequestBody(
    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password")
    @Expose
    val password: String,

    @SerializedName("password_confirmation")
    @Expose
    val passwordConfirmation: String
)