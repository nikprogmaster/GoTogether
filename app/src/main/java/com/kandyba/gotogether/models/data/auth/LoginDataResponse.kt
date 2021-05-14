package com.kandyba.gotogether.models.data.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель ответа при авторизации (data-слой)
 *
 * @property userId id пользователя
 * @property token токен сессии пользователя
 */
data class LoginDataResponse(
    @SerializedName("id")
    @Expose
    var userId: String,
    @SerializedName("ssid")
    @Expose
    var token: String
)

