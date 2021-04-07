package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Кандыба Никита
 * @since 26.03.2021
 */
data class EventComplaintRequestBody(
    @SerializedName("text")
    @Expose
    val text: String
)