package com.kandyba.gotogether.models.data.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignupDataResponse(
    @SerializedName("id")
    @Expose
    var id: String
)


