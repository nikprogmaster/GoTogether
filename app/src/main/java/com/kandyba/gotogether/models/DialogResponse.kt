package com.kandyba.gotogether.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель ответа при получении и создании диалогов
 */
data class DialogResponse(
    @SerializedName("id")
    @Expose
    val id: String
)