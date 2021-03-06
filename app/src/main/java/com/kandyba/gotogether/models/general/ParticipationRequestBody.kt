package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ParticipationRequestBody(
    @SerializedName("probability")
    @Expose
    val probability: Int
)