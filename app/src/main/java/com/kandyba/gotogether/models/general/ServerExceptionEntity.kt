package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель ошибки с бэка
 *
 * @constructor
 * @property detail текст ошибки
 */
data class NetworkError(
    @SerializedName("message")
    @Expose
    var detail: String? = null
)