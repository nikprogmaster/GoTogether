package com.kandyba.gotogether.models.general.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель тела запроса для входа
 *
 * @constructor
 * @property email почта пользователя
 * @property password пароль
 */
data class LoginRequestBody(
    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password")
    @Expose
    val password: String
)