package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("message")
    @Expose
    var detail: String? = null
)