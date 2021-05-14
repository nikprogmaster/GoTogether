package com.kandyba.gotogether.models.general.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель тела запроса жалобы
 *
 * @constructor
 * @property text текст жалобы
 */
data class EventComplaintRequestBody(
    @SerializedName("text")
    @Expose
    val text: String
)