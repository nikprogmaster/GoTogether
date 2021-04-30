package com.kandyba.gotogether.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DialogResponse(
    @SerializedName("id")
    @Expose
    val id: String
)